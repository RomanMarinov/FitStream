plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.dagger.hilt)
    id("kotlin-kapt")
    id("kotlin-parcelize")
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // retrofit 2
    implementation("com.squareup.retrofit2:retrofit:3.0.0")
    implementation("com.squareup.retrofit2:converter-gson:3.0.0")
//    implementation("com.google.code.gson:gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // okhttp
    implementation("com.squareup.okhttp3:okhttp:4.12.0")

    // Kotlin serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.1")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
//    implementation("com.google.android.exoplayer:exoplayer:2.19.1")

    implementation("androidx.media3:media3-exoplayer:1.7.1")
    implementation("androidx.media3:media3-exoplayer-dash:1.7.1")
    implementation("androidx.media3:media3-ui:1.7.1")

//    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.24")
   // implementation("org.jetbrains.kotlin:kotlin-stdlib:2.1.21")

//    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.8.10")
    // glide
    implementation("com.github.bumptech.glide:glide:4.16.0")
//    implementation("com.github.bumptech.glide:annotations:4.16.0")
//    kapt('com.github.bumptech.glide:compiler:4.12.0')

//    implementation("com.google.dagger:hilt-android:2.56.2") // или другая версия, но должна быть одинаковой
//    kapt("com.google.dagger:hilt-compiler:2.56.2") // обязательно
    implementation("com.google.dagger:hilt-android:2.51.1") // или другая версия, но должна быть одинаковой
    kapt("com.google.dagger:hilt-compiler:2.51.1") // обязательно

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.2")

    // lottie
    implementation("com.airbnb.android:lottie:6.4.0")

    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.9.0")

//    // WorkManager
//    implementation "androidx.work:work-runtime-ktx:2.8.1"
//    implementation "androidx.hilt:hilt-work:1.0.0"
//    implementation "com.google.guava:listenablefuture:9999.0-empty-to-avoid-conflict-with-guava"
//    kapt 'androidx.hilt:hilt-compiler:1.0.0'
//    implementation("androidx.core:core:1.15.0")
//    implementation("androidx.core:core-ktx:1.15.0")

}