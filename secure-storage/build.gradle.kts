plugins {
    alias(libs.plugins.compose.system.android.library)
}

group = providers.gradleProperty("GROUP").get()
version = providers.gradleProperty("VERSION_NAME").get()

android {
    namespace = "io.github.maniramezan.compose.securestorage"

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

dependencies {
    api(libs.tink.android)

    testImplementation(libs.junit.jupiter)
    testImplementation(libs.truth)
    // Robolectric (JUnit 4) covers the SharedPreferences-backed persistence seam.
    testImplementation(libs.junit)
    testImplementation(libs.robolectric)
    testImplementation(libs.androidx.test.ext.junit)
    testRuntimeOnly(libs.junit.platform.launcher)
    // Vintage engine runs the JUnit 4 / Robolectric tests alongside the Jupiter ones in a
    // single testDebugUnitTest pass.
    testRuntimeOnly(libs.junit.vintage.engine)
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}
