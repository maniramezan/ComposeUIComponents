plugins {
    alias(libs.plugins.compose.system.compose.library)
}

group = providers.gradleProperty("GROUP").get()
version = providers.gradleProperty("VERSION_NAME").get()

android {
    namespace = "io.github.maniramezan.compose.components"
}

dependencies {
    implementation(project(":theme"))
    implementation(project(":icons"))
    implementation(project(":compose-utils"))
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
}
