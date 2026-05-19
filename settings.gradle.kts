pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "ComposeUISystemDesign"

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
