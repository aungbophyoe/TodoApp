plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.aungbophyoe.data"
    compileSdk = Sdk.compile

    defaultConfig {
        applicationId = "com.aungbophyoe.data"
        minSdk = Sdk.min
        targetSdk = Sdk.target
        versionCode = Sdk.versionCode
        versionName = Sdk.versionName
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = Config.sourceCompatibility
        targetCompatibility = Config.targetCompatibility
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

kapt {
    correctErrorTypes = true
}

dependencies {

    implementation(Lib.core_ktx)
    implementation(Lib.app_compat)
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation(Lib.dagger_hilt_android)
    kapt(Lib.kapt_hilt_android_compiler)

    implementation(Lib.room_runtime)
    annotationProcessor(Lib.room_compiler)
    kapt(Lib.kapt_room_compiler)
    implementation(Lib.room_ktx)
}