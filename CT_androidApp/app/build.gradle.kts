plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    // Make sure that you have the Google services Gradle plugin
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics") // Crashlytics Gradle plugin
    id("org.jlleitschuh.gradle.ktlint") version "12.1.2" // Lint
    id("com.github.ben-manes.versions") version "0.52.0" // Dependency version management
}

android {
    namespace = "com.giovaldes.calorietracker"
    compileSdk = 35

    signingConfigs {
        create("release") {
            storeFile = file(System.getenv("MYAPP_KEYSTORE_PATH") ?: project.findProperty("MYAPP_KEYSTORE_PATH") as String)
            storePassword = System.getenv("MYAPP_KEYSTORE_PASSWORD") ?: project.findProperty("MYAPP_KEYSTORE_PASSWORD") as String
            keyAlias = System.getenv("MYAPP_KEY_ALIAS") ?: project.findProperty("MYAPP_KEY_ALIAS") as String
            keyPassword = System.getenv("MYAPP_KEY_PASSWORD") ?: project.findProperty("MYAPP_KEY_PASSWORD") as String
        }
    }

    defaultConfig {
        applicationId = "com.giovaldes.calorietracker"
        minSdk = 23
        targetSdk = 35
        versionCode = 2
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
        debug {
            versionNameSuffix = ".debug"
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
        compose = true
    }
    flavorDimensions += listOf("enviroment")
    productFlavors {
        create("free") {
            dimension = "enviroment"
            applicationId = "com.giovaldes.calorietracker"
            manifestPlaceholders["appIcon"] = "@mipmap/ic_launcher_free"
        }
        create("full") {
            dimension = "enviroment"
            applicationId = "com.giovaldes.calorietracker"
        }
    }
}

dependencies {
    implementation(platform(libs.firebase.bom))
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.firebase.core)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.config)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.okhttp)
    implementation(libs.ui.tooling.preview)
    implementation(libs.androidx.foundation)
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test) // For coroutine testing
    testImplementation(libs.turbine) // For state flow testing
    testImplementation(libs.mockwebserver) // For unit test
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    // Dependency integrations test
    androidTestImplementation(libs.mockwebserver)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.runner)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.rules)
    androidTestImplementation(libs.ui.test.junit4)
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17) // Usa Java 17
    }
}
