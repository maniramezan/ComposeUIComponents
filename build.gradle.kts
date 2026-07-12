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

tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
    jvmTarget = "17"
}

abstract class DownloadBaselineAar : DefaultTask() {
    @get:Input
    abstract val url: Property<String>

    @get:OutputFile
    abstract val outputFile: RegularFileProperty

    @TaskAction
    fun download() {
        val destination = outputFile.get().asFile
        destination.parentFile.mkdirs()
        URI(url.get()).toURL().openStream().use { input ->
            Files.copy(input, destination.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING)
        }
    }
}

subprojects {
    pluginManager.apply("io.gitlab.arturbosch.detekt")
    pluginManager.apply("org.jlleitschuh.gradle.ktlint")

    extensions.configure<io.gitlab.arturbosch.detekt.extensions.DetektExtension> {
        buildUponDefaultConfig = true
        config.setFrom(rootProject.files("config/detekt/detekt.yml"))
        parallel = true
    }

    tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
        jvmTarget = "17"
    }

    pluginManager.withPlugin("com.android.library") {
        pluginManager.apply("org.jetbrains.dokka")
        pluginManager.apply("com.vanniktech.maven.publish")
        pluginManager.apply("me.champeau.gradle.japicmp")

        val artifactName = project.name
        val baselineVersion = providers.gradleProperty("API_BASELINE_VERSION").get()
        val groupPath = rootProject.providers.gradleProperty("GROUP").get().replace('.', '/')
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
                oldClasspath.from(extractBaseline.map { File(it.destinationDir, "classes.jar") })
                newClasspath.from(extractCurrent.map { File(it.destinationDir, "classes.jar") })
                onlyModified = true
                onlyBinaryIncompatibleModified = true
                failOnModification = true
                ignoreMissingClasses = true
                txtOutputFile =
                    layout.buildDirectory.file("reports/binary-compatibility/japicmp.txt").get().asFile
            }
        tasks.named("check").configure { dependsOn(binaryCompatibilityCheck) }
    }
    pluginManager.withPlugin("org.jetbrains.kotlin.multiplatform") {
        pluginManager.apply("org.jetbrains.dokka")
        pluginManager.apply("com.vanniktech.maven.publish")
    }
}
