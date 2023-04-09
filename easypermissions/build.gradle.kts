@file:Suppress("UnstableApiUsage")

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("kotlin-parcelize")
    `maven-publish`
}

group = "com.github.zhaoyingang"

android {
    compileSdk = 33

    defaultConfig {
        minSdk = 14
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }

    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = listOf("-Xcontext-receivers")
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }
}

publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = "com.github.zhaoyingang"
            artifactId = "EasierPermissions"
            version = "1.0.0"

            afterEvaluate {
                from(components["release"])
            }
        }
    }

    repositories {
        maven(url = "$rootDir/maven")
    }
}

dependencies {
    api("androidx.annotation:annotation:1.6.0")
    api("androidx.appcompat:appcompat:1.6.1")
    api("androidx.core:core:1.9.0")
    api("androidx.fragment:fragment:1.5.6")

    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.8.10")
    testImplementation("junit:junit:4.13.2")
    testImplementation("com.google.truth:truth:1.0")
    testImplementation("org.robolectric:robolectric:4.4")
    testImplementation("androidx.test:core:1.5.0")
    testImplementation("androidx.fragment:fragment-testing:1.4.1")
    testImplementation("org.mockito:mockito-core:3.0.0")
}
