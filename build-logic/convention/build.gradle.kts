plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(libs.android.gradle.plugin)
    implementation(libs.kotlin.gradle.plugin)
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "compose.system.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidLibrary") {
            id = "compose.system.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("composeLibrary") {
            id = "compose.system.compose.library"
            implementationClass = "ComposeLibraryConventionPlugin"
        }
        register("kotlinLibrary") {
            id = "compose.system.kotlin.library"
            implementationClass = "KotlinLibraryConventionPlugin"
        }
        register("kotlinMultiplatformLibrary") {
            id = "compose.system.kotlin.multiplatform.library"
            implementationClass = "KotlinMultiplatformLibraryConventionPlugin"
        }
    }
}
