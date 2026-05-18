plugins {
    alias(libs.plugins.binary.compatibility.validator)
    alias(libs.plugins.detekt) apply false
    alias(libs.plugins.dokka) apply false
    alias(libs.plugins.ktlint)
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.maven.publish) apply false
    alias(libs.plugins.roborazzi) apply false
}

apiValidation {
    ignoredProjects.addAll(listOf("catalog", "sample", "testing", "tokens", "theme", "icons", "compose-utils", "components"))
}

tasks.register("composeCompilerReports") {
    group = "verification"
    description = "Enable with -PenableComposeCompilerReports=true on assemble tasks."
}

tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
    jvmTarget = "17"
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
    }
    pluginManager.withPlugin("org.jetbrains.kotlin.multiplatform") {
        pluginManager.apply("org.jetbrains.dokka")
        pluginManager.apply("com.vanniktech.maven.publish")
    }
}
