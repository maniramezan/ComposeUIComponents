pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "ComposeUIComponents"

include(":foundation")
include(":tokens")
include(":theme")
include(":icons")
include(":compose-utils")
include(":components")
include(":testing")
include(":catalog")
include(":sample")
include(":baselineprofile")
