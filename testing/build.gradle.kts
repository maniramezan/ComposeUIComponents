plugins {
    alias(libs.plugins.compose.system.compose.library)
}

group = providers.gradleProperty("GROUP").get()
version = providers.gradleProperty("VERSION_NAME").get()

android {
    namespace = "io.github.maniramezan.compose.testing"
}

dependencies {
    api(project(":compose-utils"))
    implementation(platform(libs.androidx.compose.bom))
    api(libs.androidx.compose.ui.test.junit4)
}
