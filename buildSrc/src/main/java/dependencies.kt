import org.gradle.api.JavaVersion

object Sdk {
    const val compile = 34
    const val min = 24
    const val target = 33
    const val versionCode = 1
    const val versionName = "1.0.0"
}

object Config {
    val sourceCompatibility = JavaVersion.VERSION_1_8
    val targetCompatibility = JavaVersion.VERSION_1_8
    val jvmTarget = "1.8"
    val isMinifyEnabled = false
    var debug_enable = false
    const val hilt = "com.google.dagger:hilt-android-gradle-plugin:2.48"
}

object Lib {
    private object Version {
        const val hilt = "2.48"
        const val room = "2.6.1"
        const val lifecycle_version = "2.6.2"
        const val orbit = "4.2.0"
        const val dateTime = "0.2.1"
        const val ktx = "1.9.0"
        const val coroutines_version = "1.6.0"
        const val appcompat = "1.6.1"
        const val nav_version = "2.7.5"
    }
    const val core_ktx = "androidx.core:core-ktx:${Version.ktx}"
    const val app_compat = "androidx.appcompat:appcompat:${Version.appcompat}"
    const val dagger_hilt_android = "com.google.dagger:hilt-android:${Version.hilt}"
    const val kapt_hilt_android_compiler = "com.google.dagger:hilt-android-compiler:${Version.hilt}"
    const val room_runtime = "androidx.room:room-runtime:${Version.room}"
    const val room_compiler = "androidx.room:room-compiler:${Version.room}"
    const val kapt_room_compiler = "androidx.room:room-compiler:${Version.room}"
    const val room_ktx = "androidx.room:room-ktx:${Version.room}"
    const val lifecycle_viewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Version.lifecycle_version}"
    const val lifecycle_livedata = "androidx.lifecycle:lifecycle-livedata-ktx:${Version.lifecycle_version}"

    const val orbit_mvi = "org.orbit-mvi:orbit-core:${Version.orbit}"
    const val orbit_mvi_viewModel = "org.orbit-mvi:orbit-viewmodel:${Version.orbit}"
    const val date_time = "org.jetbrains.kotlinx:kotlinx-datetime:${Version.dateTime}"
    const val std_lib = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Version.ktx}"
    const val coroutines_core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Version.coroutines_version}"
    const val coroutines_android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Version.coroutines_version}"

    const val progress = "com.kaopiz:kprogresshud:1.2.0"

    const val navigation_fragment = "androidx.navigation:navigation-fragment-ktx:${Version.nav_version}"
    const val navigation_ui = "androidx.navigation:navigation-ui-ktx:${Version.nav_version}"
}