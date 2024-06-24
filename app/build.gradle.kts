plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("kotlin-parcelize")
    kotlin("plugin.serialization") version "2.0.0"
}

android {
    namespace = "com.bogdash.cocktails"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.bogdash.cocktails"
        minSdk = 26
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.android.shape.imageview)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Domain module
    implementation(project(":domain"))

    // Data module
    implementation(project(":data"))

    //Lottie animation
    implementation(libs.lottie)

    // Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation(libs.androidx.fragment.ktx.v170)

    // Glide
    implementation (libs.glide)

    //Retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit2.converter.gson)

    // Room
    implementation (libs.androidx.room.runtime)
    kapt (libs.androidx.room.compiler)

    // Serialization
    implementation(libs.kotlinx.serialization.json)

    // QR
    implementation(libs.zxing.android.embedded)
}