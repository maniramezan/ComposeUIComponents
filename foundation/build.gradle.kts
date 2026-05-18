plugins {
    alias(libs.plugins.compose.system.kotlin.library)
}

group = providers.gradleProperty("GROUP").get()
version = providers.gradleProperty("VERSION_NAME").get()
