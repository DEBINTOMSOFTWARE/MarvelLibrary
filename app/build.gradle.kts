import java.io.FileInputStream
import java.util.Properties
import kotlin.properties.ReadOnlyProperty

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.example.marvellibrary"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.marvellibrary"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        buildFeatures {
            buildConfig = true
        }

        // Read Marvel API keys from apikey.properties
        val apiKeysFile = file("apikey.properties")
        val apiKeys = Properties()

        if (apiKeysFile.exists()) {
            apiKeys.load(apiKeysFile.inputStream())
        } else {
            throw GradleException("API keys file not found: $apiKeysFile")
        }

        // Retrieve Marvel API keys
        val marvelApiKey: String = apiKeys.getProperty("MARVEL_KEY")
            ?: throw GradleException("marvel_key not found in apikey.properties")
        val marvelApiSecret: String = apiKeys.getProperty("MARVEL_SECRET")
            ?: throw GradleException("marvel_secret not found in apikey.properties")

        // Set BuildConfig fields
        buildConfigField("String", "MARVEL_KEY", "\"$marvelApiKey\"")
        buildConfigField("String", "MARVEL_SECRET", "\"$marvelApiSecret\"")
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
val core_ktx = "1.10.1"
val nav_version = "2.7.6"
val coil_version = "2.1.0"
val lifecycle_version = "2.5.1"
val retrofit_version = "2.9.0"
val hilt_version = "2.50"
val room_version = "2.4.2"

dependencies {

    implementation("androidx.core:core-ktx:$core_ktx")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.activity:activity-compose:1.7.0")
    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")

    implementation("androidx.compose.material3:material3")
    implementation ("androidx.compose.material:material:1.1.1")
    implementation("androidx.navigation:navigation-compose:$nav_version")
    implementation("io.coil-kt:coil-compose:$coil_version")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:$lifecycle_version")
    implementation("com.squareup.retrofit2:retrofit:$retrofit_version")
    implementation("com.squareup.retrofit2:converter-gson:$retrofit_version")
    implementation("com.google.dagger:hilt-android:$hilt_version")
    kapt("com.google.dagger:hilt-compiler:$hilt_version")
    implementation ("androidx.room:room-runtime:$room_version")
    kapt ("androidx.room:room-compiler:$room_version")
    implementation ("androidx.room:room-ktx:$room_version")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}