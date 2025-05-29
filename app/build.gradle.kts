plugins {
//    alias(libs.plugins.android.application)
//    alias(libs.plugins.kotlin.android)
    id("com.google.gms.google-services")

//    id("com.android.application")
    id("org.jetbrains.kotlin.android") version "2.1.0" // Must match project-level Kotlin version
//
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.0"
//    kotlin("plugin.serialization") version "2.1.0"
//


    alias(libs.plugins.android.application)
//    alias(libs.plugins.kotlin.android)

    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")

}

android {
    namespace = "com.example.fitup"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.fitup"
        minSdk = 31
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(platform("com.google.firebase:firebase-bom:33.11.0"))
    implementation( "org.jetbrains.androidx.navigation:navigation-compose:2.8.0-alpha01")

    implementation("androidx.datastore:datastore-preferences:1.1.1")
    implementation("com.google.firebase:firebase-database-ktx:20.1.0")

    // Optional: For coroutines support
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.0")
    implementation("com.google.firebase:firebase-storage-ktx:20.1.0") // delete
    implementation("com.google.firebase:firebase-analytics")
    implementation("io.coil-kt:coil-compose:2.2.2")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.firebase.auth.ktx)
    implementation(libs.androidx.media3.common.ktx)
    //implementation(libs.androidx.compose.testing)
//    implementation(libs.firebase.auth.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

//    implementation(platform("io.github.jan-tennert.supabase:bom:2.3.0"))
//
//    // Supabase modules (versions will be inherited from the BOM)
//    implementation("io.github.jan-tennert.supabase:postgrest-kt")
//    implementation("io.github.jan-tennert.supabase:gotrue-kt") // Note: Changed from auth-kt
//    implementation("io.github.jan-tennert.supabase:realtime-kt")
//    //implementation("io.github.jan-tennert.supabase:compose-auth:3.1.1")

    // Required for Android
//    val ktorVersion = "2.3.7"
//
//    implementation("io.ktor:ktor-client-core:$ktorVersion")
//    implementation("io.ktor:ktor-client-android:$ktorVersion")
//    implementation("io.ktor:ktor-client-okhttp:$ktorVersion")
//    implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
//    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
//    implementation("io.ktor:ktor-client-logging:$ktorVersion")
//
//    implementation("org.jetbrains.kotlin:kotlin-stdlib:2.0.0")





    // hilt
    implementation("com.google.dagger:hilt-android:2.56.1")
    kapt("com.google.dagger:hilt-android-compiler:2.56.1")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")

}