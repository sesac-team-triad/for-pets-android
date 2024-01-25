// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.2.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.10" apply false
    id("com.google.gms.google-services") version "4.4.0" apply false
    id("com.google.devtools.ksp") version "1.9.10-1.0.13" apply false
}

buildscript {
    dependencies {
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.7.6")
    }
}