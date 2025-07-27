plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.kapt")
    id("com.google.dagger.hilt.android")
    id("org.jetbrains.kotlin.plugin.compose")
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

    buildFeatures {
        compose = true
    }
    composeOptions {
        // With the plugin, you don’t need to specify this.
        // kotlinCompilerExtensionVersion = "1.4.7"
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    // Core
    implementation(libs.androidx.core.ktx)

    // Compose
    implementation(libs.androidx.activity.compose)
    implementation(libs.ui)                              // androidx.compose.ui:ui
    implementation(libs.material3)                       // androidx.compose.material3:material3
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    // Hilt
    implementation(libs.dagger.hilt.android)
    kapt(libs.dagger.hilt.android.compiler)                            // com.google.dagger:hilt-android-compiler

    // Room & DataStore
    implementation(libs.androidx.room.runtime)
    kapt(libs.androidx.room.compiler)
    implementation(libs.androidx.datastore.preferences)

    // Media3 ExoPlayer (stable)
    implementation(libs.androidx.media3.exoplayer.v171)
    implementation(libs.androidx.media3.ui.v171)
    implementation(libs.androidx.media3.downloader.v171)

    val media3Version = "1.7.1"

    implementation(libs.androidx.media3.exoplayer.v171)
    implementation(libs.androidx.media3.ui.v171)
    implementation(libs.androidx.media3.downloader.v171)
}
