// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false

    // Make sure that you have the Google services Gradle plugin 4.4.1+ dependency
    id("com.google.gms.google-services") version "4.4.2" apply false

    id("com.google.firebase.crashlytics") version "3.0.3" apply false // Crashlytics Gradle plugin
    id("com.github.ben-manes.versions") version "0.52.0" // Dependency version management
}


