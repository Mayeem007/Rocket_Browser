plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
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
    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.compose.ui:ui:1.4.3")
    implementation("androidx.compose.material3:material3:1.1.0")
    implementation("androidx.activity:activity-compose:1.7.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1")
    implementation("com.google.dagger:hilt-android:2.47")
    kapt("com.google.dagger:hilt-android-compiler:2.47")
    // Media3 ExoPlayer
    implementation("androidx.media3:media3-exoplayer:1.1.0-beta02")
    implementation("androidx.media3:media3-ui:1.1.0-beta02")
    implementation("androidx.media3:media3-downloader:1.1.0-beta02")
    implementation("androidx.datastore:datastore-preferences:1.1.0-alpha05")
    implementation("androidx.room:room-runtime:2.6.0-alpha02")
    kapt("androidx.room:room-compiler:2.6.0-alpha02")
}
