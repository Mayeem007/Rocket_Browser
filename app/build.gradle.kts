plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
    id("org.jetbrains.kotlin.plugin.compose")   // <-- Compose Compiler plugin
    kotlin("kapt")
}

android {
    namespace = "com.example.rocketbrowser"
    compileSdk = 36
    defaultConfig {
        applicationId = "com.example.rocketbrowser"
        minSdk = 21
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
    }
    buildFeatures { compose = true }
    composeOptions { kotlinCompilerExtensionVersion = "1.4.7" }
    kotlinOptions { jvmTarget = "11" }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.ui)
    implementation(libs.material3)
    implementation(libs.androidx.activity.compose.v172)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.hilt.android.v247)
    kapt(libs.hilt.android.compiler.v247)
    // Media3 ExoPlayer
    implementation(libs.androidx.media3.exoplayer)
    implementation(libs.dagger.hilt.android)
    kapt(libs.dagger.hilt.android.compiler)
    implementation(libs.androidx.media3.ui.v110beta02)
    implementation(libs.androidx.media3.downloader.v110beta02)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.room.runtime)
    kapt(libs.androidx.room.compiler)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.ui.v143)
    implementation(libs.androidx.material3.v110)
    implementation(libs.androidx.activity.compose)

    // Use the latest stable Media3 release instead of unreleased beta
    val media3Version = "1.7.1"                                   // stable as of July 24, 2025[3]
    implementation(libs.androidx.media3.exoplayer.v171)
    implementation(libs.androidx.media3.ui.v171)
    implementation(libs.androidx.media3.downloader.v171)

    // Hilt, Room, Coroutines, etc.
    implementation(libs.dagger.hilt.android)
    kapt(libs.dagger.hilt.android.compiler)
}
