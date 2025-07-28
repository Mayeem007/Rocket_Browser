plugins {
    alias(libs.plugins.android-application) apply false
    alias(libs.plugins.kotlin-android) apply false
}

buildscript {
    repositories {
        google()
        mavenCentral()
    }
}

// allprojects block removed - repositories are configured in settings.gradle.kts
