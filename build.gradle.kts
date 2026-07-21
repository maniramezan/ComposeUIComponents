import java.net.URI
import java.nio.file.Files

plugins {
    id("compose.system.check.component-token-usage")
    alias(libs.plugins.binary.compatibility.validator)
    alias(libs.plugins.japicmp) apply false
    alias(libs.plugins.detekt) apply false
    alias(libs.plugins.dokka) apply false
    alias(libs.plugins.ktlint)
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.test) apply false
    alias(libs.plugins.androidx.baselineprofile) apply false
    alias(libs.plugins.maven.publish) apply false
    alias(libs.plugins.roborazzi) apply false
}

apiValidation {
    ignoredProjects.addAll(
        listOf(
            "baselineprofile",
            "catalog",
            "sample",
        ),
    )
}

tasks.register("composeCompilerReports") {
    group = "verification"
    description = "Generate Compose compiler metrics and reports for Compose modules."
    dependsOn(
        ":tokens:assembleDebug",
        ":theme:assembleDebug",
        ":icons:assembleDebug",
        ":compose-utils:assembleDebug",
        ":components:assembleDebug",
        ":catalog:assembleDebug",
        ":sample:assembleDebug",
    )
}

tasks.withType<dev.detekt.gradle.Detekt>().configureEach {
    jvmTarget.set("17")
}

abstract class DownloadBaselineAar : DefaultTask() {
    @get:Input
    abstract val url: Property<String>

    @get:OutputFile
    abstract val outputFile: RegularFileProperty

    /** True after [download] runs iff the baseline artifact actually exists and was fetched. */
    @get:Internal
    abstract val baselineExists: Property<Boolean>

    @TaskAction
    fun download() {
        val destination = outputFile.get().asFile
        destination.parentFile.mkdirs()
        try {
            URI(url.get()).toURL().openStream().use { input ->
                Files.copy(input, destination.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING)
            }
            baselineExists.set(true)
        } catch (e: java.io.FileNotFoundException) {
            // No published artifact at this API_BASELINE_VERSION yet — this is expected right
            // after bumping API_BASELINE_VERSION ahead of an intentional (pre-1.0) breaking
            // release, before that version has actually been published to Maven Central.
            // There is nothing to compare against yet, so skip the comparison for this build
            // instead of failing; once the version is published, this resumes enforcing
            // normally against the new baseline.
            logger.lifecycle(
                "No binary-compatibility baseline artifact found at ${url.get()} " +
                    "(HTTP 404) — skipping binaryCompatibilityCheck for this module.",
            )
            baselineExists.set(false)
        }
    }
}

subprojects {
    pluginManager.apply("dev.detekt")
    pluginManager.apply("org.jlleitschuh.gradle.ktlint")

    extensions.configure<dev.detekt.gradle.extensions.DetektExtension> {
        buildUponDefaultConfig = true
        config.setFrom(rootProject.files("config/detekt/detekt.yml"))
        parallel = true
    }

    tasks.withType<dev.detekt.gradle.Detekt>().configureEach {
        jvmTarget.set("17")
    }

    pluginManager.withPlugin("com.android.library") {
        pluginManager.apply("org.jetbrains.dokka")
        pluginManager.apply("com.vanniktech.maven.publish")
        pluginManager.apply("me.champeau.gradle.japicmp")

        val artifactName = project.name
        val baselineVersion = providers.gradleProperty("API_BASELINE_VERSION").get()
        val groupPath =
            rootProject.providers
                .gradleProperty("GROUP")
                .get()
                .replace('.', '/')
        val downloadBaseline =
            tasks.register<DownloadBaselineAar>("downloadBinaryCompatibilityBaseline") {
                url.set(
                    "https://repo.maven.apache.org/maven2/$groupPath/$artifactName/$baselineVersion/" +
                        "$artifactName-$baselineVersion.aar",
                )
                outputFile.set(layout.buildDirectory.file("binary-compatibility/$artifactName-$baselineVersion.aar"))
            }
        val extractBaseline =
            tasks.register<Sync>("extractBinaryCompatibilityBaseline") {
                dependsOn(downloadBaseline)
                val baselineExists = downloadBaseline.flatMap { it.baselineExists }
                onlyIf { baselineExists.getOrElse(false) }
                from(downloadBaseline.map { zipTree(it.outputFile) }) {
                    include("classes.jar")
                }
                into(layout.buildDirectory.dir("binary-compatibility/baseline"))
            }
        val extractCurrent =
            tasks.register<Sync>("extractBinaryCompatibilityCurrent") {
                from(
                    tasks.named("bundleReleaseAar").map { bundleAar ->
                        zipTree(bundleAar.outputs.files.singleFile)
                    },
                ) {
                    include("classes.jar")
                }
                into(layout.buildDirectory.dir("binary-compatibility/current"))
            }

        val binaryCompatibilityCheck =
            tasks.register<me.champeau.gradle.japicmp.JapicmpTask>("binaryCompatibilityCheck") {
                dependsOn(extractBaseline, extractCurrent)
                // No baseline artifact exists yet at the current API_BASELINE_VERSION (e.g. it was
                // just bumped ahead for an intentional pre-1.0 breaking release that hasn't
                // published yet) — nothing to compare against, so skip rather than fail. See
                // DownloadBaselineAar above.
                val baselineExists = downloadBaseline.flatMap { it.baselineExists }
                onlyIf { baselineExists.getOrElse(false) }
                oldClasspath.from(extractBaseline.map { File(it.destinationDir, "classes.jar") })
                newClasspath.from(extractCurrent.map { File(it.destinationDir, "classes.jar") })
                onlyModified = true
                onlyBinaryIncompatibleModified = true
                failOnModification = true
                ignoreMissingClasses = true
                txtOutputFile =
                    layout.buildDirectory
                        .file("reports/binary-compatibility/japicmp.txt")
                        .get()
                        .asFile
            }
        tasks.named("check").configure { dependsOn(binaryCompatibilityCheck) }
    }
    pluginManager.withPlugin("org.jetbrains.kotlin.multiplatform") {
        pluginManager.apply("org.jetbrains.dokka")
        pluginManager.apply("com.vanniktech.maven.publish")
    }
}
