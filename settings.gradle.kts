pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    plugins {
        id("com.android.application") version "8.11.1" apply false
        id("org.jetbrains.kotlin.android") version "2.2.0" apply false
        id("org.jetbrains.kotlin.kapt") version "2.2.0" apply false
        id("com.google.dagger.hilt.android") version "2.57" apply false
        id("org.jetbrains.kotlin.plugin.compose") version "2.2.0" apply false
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
