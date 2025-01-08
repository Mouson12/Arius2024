plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
//    id("kotlin-kapt")
//    id("com.google.devtools.ksp")
    id("com.google.devtools.ksp") version "1.9.10-1.0.13" apply true
}

android {
    namespace = "com.example.garage"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.garage"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        javaCompileOptions {
            annotationProcessorOptions {
                arguments += mapOf(
                    "room.schemaLocation" to "$projectDir/schemas".toString(),
                    "room.incremental" to "true",
                    "room.expandProjection" to "true"
                )
            }
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
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.runtime.android)
//    implementation(libs.androidx.material3.android)
//    implementation(libs.androidx.ui.android)
//    implementation(libs.androidx.navigation.compose)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
//    kapt(libs.androidx.room.compiler)
    ksp(libs.androidx.room.compiler.v250)
    implementation("com.applandeo:material-calendar-view:1.9.2")
    implementation("androidx.room:room-ktx:2.5.0")
    implementation("androidx.compose.ui:ui:1.5.0")
    implementation("androidx.compose.material3:material3:1.1.0")
    implementation("androidx.activity:activity-compose:1.7.0")


//    ksp("androidx.room:room-compiler:2.5.0")
//    kapt("androidx.room:room-compiler:2.5.0")
}