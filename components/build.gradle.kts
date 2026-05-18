plugins {
    alias(libs.plugins.compose.system.compose.library)
    alias(libs.plugins.roborazzi)
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
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.foundation.layout)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.showkase.annotation)

    testImplementation(libs.junit.jupiter)
    testImplementation(libs.junit)
    testImplementation(libs.robolectric)
    testImplementation(libs.roborazzi.compose)
    testImplementation(libs.roborazzi.junit.rule)
    testImplementation(libs.androidx.compose.ui.test.junit4)
    testImplementation(libs.truth)
    testRuntimeOnly(libs.junit.platform.launcher)
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

tasks.matching { it.name == "testDebugUnitTest" }.configureEach {
    (this as Test).useJUnit()
}
