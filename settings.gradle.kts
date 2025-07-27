pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    plugins {
        // Register Compose compiler plugin for Kotlin 2.0+
        id("org.jetbrains.kotlin.plugin.compose") version "2.2.0"
        // Register Hilt plugin
        id("com.google.dagger.hilt.android") version "2.57"
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Rocket_Browser"
include(":app")
