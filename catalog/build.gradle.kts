plugins {
    alias(libs.plugins.compose.system.android.application)
}

android {
    namespace = "io.github.maniramezan.compose.catalog"

    defaultConfig {
        applicationId = "io.github.maniramezan.compose.catalog"
        versionCode = 1
        versionName = providers.gradleProperty("VERSION_NAME").get()
    }
}

dependencies {
    implementation(project(":foundation"))
    implementation(project(":tokens"))
    implementation(project(":theme"))
    implementation(project(":icons"))
    implementation(project(":compose-utils"))
    implementation(project(":components"))
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling.preview)
}
