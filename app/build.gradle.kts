plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    namespace = "com.example.rocketbrowser"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.rocketbrowser"
        minSdk = 34
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = false // Set to true ONLY if using Jetpack Compose UI
    }
}

dependencies {
    // Core Android UI, Material Design, and Jetpack libraries
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.activity:activity-ktx:1.7.2")

    // ExoPlayer (video player; you do NOT need the non-existent extension-downloader)
    implementation("com.google.android.exoplayer:exoplayer:2.18.5")
    implementation("com.google.android.exoplayer:extension-okhttp:2.18.5")

    // Testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        // (optionally) others like maven { url = ... }
    }
}
