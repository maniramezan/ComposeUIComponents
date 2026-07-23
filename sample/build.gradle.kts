plugins {
    alias(libs.plugins.compose.system.android.application)
    alias(libs.plugins.androidx.baselineprofile)
}

android {
    namespace = "io.github.maniramezan.compose.sample"

    defaultConfig {
        applicationId = "io.github.maniramezan.compose.sample"
        versionCode = 1
        versionName = providers.gradleProperty("VERSION_NAME").get()
    }
}

// Generates/merges sample/src/main/baseline-prof.txt from the :baselineprofile
// module's BaselineProfileGenerator. See docs/performance.md and
// docs/release.md's "Baseline profile review" 1.0.0 gate item.
baselineProfile {
    // Single build-type dimension (debug/release only, no flavors) — merge the
    // generated profile straight into src/main/baseline-prof.txt instead of a
    // per-variant src/release/ copy, so it's checked in at the conventional path.
    mergeIntoMain = true
    // Skip regenerating the profile on every debug build; run it explicitly via
    // `./gradlew :sample:generateBaselineProfile` (see docs/performance.md).
    automaticGenerationDuringBuild = false
}

dependencies {
    implementation(project(":theme"))
    implementation(project(":icons"))
    implementation(project(":components"))
    implementation(project(":compose-utils"))
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.activity.compose)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.foundation.layout)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.core)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling.preview)
    baselineProfile(project(":baselineprofile"))
}
