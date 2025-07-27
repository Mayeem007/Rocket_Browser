pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    plugins {
        // Register Compose compiler plugin for Kotlin 2.0+
        id("org.jetbrains.kotlin.plugin.compose") version "2.0.20"
        // Register Hilt plugin
        id("com.google.dagger.hilt.android") version "2.47"
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
