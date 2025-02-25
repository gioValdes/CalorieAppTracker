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

    defaultConfig {
        applicationId = "com.giovaldes.calorietracker"
        minSdk = 23
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
        getByName("debug") {
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
        buildConfig = true // Habilita la generación de BuildConfig
    }
    flavorDimensions += listOf("environment")
    productFlavors {
        create("free") {
            manifestPlaceholders += mapOf()
            dimension = "environment"
            applicationId = "com.giovaldes.calorietracker"
            manifestPlaceholders["appIcon"] = "@mipmap/ic_launcher_free"
        }
        create("full") {
            dimension = "environment"
            applicationId = "com.giovaldes.calorietracker"
            buildConfigField("String", "BASE_URL", "\"https://production.api.tuapp.com/\"")
        }
        create("staging") {
            dimension = "environment"
            applicationId = "com.giovaldes.calorietracker"
            buildConfigField("String", "BASE_URL", "\"https://staging.api.tuapp.com/\"")
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
