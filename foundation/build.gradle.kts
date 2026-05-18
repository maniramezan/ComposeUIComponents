plugins {
    alias(libs.plugins.compose.system.kotlin.multiplatform.library)
}

group = providers.gradleProperty("GROUP").get()
version = providers.gradleProperty("VERSION_NAME").get()

kotlin {
    sourceSets {
        jvmTest.dependencies {
            implementation(libs.junit.jupiter)
            implementation(libs.truth)
            runtimeOnly(libs.junit.platform.launcher)
        }
    }
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}
