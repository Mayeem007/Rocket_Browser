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
        compose = false
    }
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.profileinstaller)

    implementation(libs.material)
    implementation(libs.core.ktx.v1101)
    implementation(libs.androidx.activity.ktx)
    // ExoPlayer for video playback
    implementation(libs.exoplayer)
    implementation(libs.extension.okhttp)

    implementation(libs.androidx.media3.ui)
    implementation(libs.androidx.media3.downloader)
    // Unit Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit.v115)
    androidTestImplementation(libs.androidx.espresso.core.v351)
}
