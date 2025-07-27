import org.gradle.kotlin.dsl.implementation

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
        targetSdk = 34
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

    implementation (libs.androidx.core.ktx.v1120)
    implementation (libs.androidx.appcompat.v161)
        implementation (libs.material.v1110)
        implementation (libs.androidx.constraintlayout)

        // Navigation component
        implementation (libs.androidx.navigation.ui.ktx)

        // ViewModel and LiveData
        implementation (libs.androidx.lifecycle.viewmodel.ktx)
        implementation (libs.androidx.lifecycle.livedata.ktx)

        // Room for database
        implementation (libs.androidx.room.runtime.v261)
        implementation (libs.androidx.room.ktx)
        kapt (libs.androidx.room.compiler.v261)

        // Coroutines for async operations
        implementation (libs.kotlinx.coroutines.android)

        // Glide for image loading
        implementation (libs.glide)

        // Retrofit for network operations
        implementation (libs.retrofit)
        implementation (libs.converter.gson)

        // Testing
        testImplementation (libs.junit)
        androidTestImplementation (libs.androidx.junit)
        androidTestImplementation (libs.androidx.espresso.core)
    }

    val media3Version = "1.7.1"


