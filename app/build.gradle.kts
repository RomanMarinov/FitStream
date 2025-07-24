plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-android")
    id("kotlin-parcelize")
    id("androidx.navigation.safeargs.kotlin")
    id("org.jetbrains.kotlin.kapt")

}

android {
    namespace = "com.example.fitstream"
    compileSdk = 35

    buildFeatures {
        dataBinding = true
        buildConfig = true
    }

    defaultConfig {
        applicationId = "com.example.fitstream"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "BASE_URL", "\"https://ref.test.kolsa.ru\"")
    }

    buildTypes {
        debug {
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlinOptions {
        jvmTarget = "21"
    }
}

dependencies {
    // core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // ui
    implementation(libs.lottie)
    implementation(libs.androidx.recyclerview)

    // media
    implementation(libs.androidx.media3.exoplayer)
    implementation(libs.androidx.media3.exoplayer.dash)
    implementation(libs.androidx.media3.ui)

    // Kotlin serialization
    implementation(libs.kotlinx.serialization.json)

    // coroutine
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    implementation(libs.kotlin.stdlib)

    // legacy support
    implementation(libs.androidx.legacy.support.v4)

    // viewmodel livedata
    implementation(libs.androidx.lifecycle.livedata.ktx)

    // fragment extension
    implementation(libs.androidx.fragment.ktx)

    // Dagger core
    implementation(libs.dagger)
    kapt(libs.dagger.compiler)

    // test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockk)
    testImplementation(libs.androidx.core.testing)
    testImplementation(libs.androidx.lifecycle.runtime.testing)
    androidTestImplementation("io.mockk:mockk-android:1.13.9")
    testImplementation(kotlin("test"))
    testImplementation("junit:junit:4.13.2")

    // lifecycle viewmodel
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    // network
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)
    implementation(libs.okhttp)

    // navigation
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

}
