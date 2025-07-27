// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    id("com.google.dagger.hilt.android") version "2.57" apply false
}

buildscript {
    repositories {
        google()
        mavenCentral()
    }
}

// allprojects block removed - repositories are configured in settings.gradle.kts
allprojects {
    repositories {
        google()  // Make sure this is here
        mavenCentral()
    }
}
