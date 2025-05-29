// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
//    alias(libs.plugins.kotlin.android) apply false



    id("org.jetbrains.kotlin.android") version "2.1.0" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.0"

    id("com.google.dagger.hilt.android") version "2.56.1" apply false
    id("com.google.devtools.ksp") version "1.9.20-1.0.14" apply false
    //kotlin("jvm") version "2.0.0"
}
buildscript {
    dependencies {
        classpath("com.google.gms:google-services:4.4.1") // Latest version
        classpath("com.google.firebase:firebase-crashlytics-gradle:2.9.9") // If using Crashlytics
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.48")
    }
}