@file:Suppress("UnstableApiUsage")

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.zhaoyingang.permission.sample"
    compileSdk = 33
    defaultConfig {
        applicationId = "com.zhaoyingang.permission.sample"
        minSdk = 14
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = listOf("-Xcontext-receivers")
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(project(":xxpermissions-ktx"))
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.annotation:annotation:1.6.0")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.8.20")
    
    // Google Material Design
    implementation("com.google.android.material:material:1.9.0")

    // ViewBinding https://github.com/DylanCaiCoding/ViewBindingKTX
    implementation("com.github.DylanCaiCoding.ViewBindingKTX:viewbinding-nonreflection-ktx:2.1.0")
}
