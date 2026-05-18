plugins {
    alias(libs.plugins.binary.compatibility.validator)
    alias(libs.plugins.detekt) apply false
    alias(libs.plugins.dokka) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.maven.publish) apply false
    alias(libs.plugins.roborazzi) apply false
}

apiValidation {
    ignoredProjects.addAll(listOf("catalog", "sample", "testing"))
}

tasks.register("ktlintCheck") {
    group = "verification"
    description = "Placeholder ktlint gate until formatting is wired in Phase 1."
}

tasks.register("composeCompilerReports") {
    group = "verification"
    description = "Enable with -PenableComposeCompilerReports=true on assemble tasks."
}
