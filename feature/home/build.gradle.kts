plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.sesac.home"
    compileSdk = 36

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
//    kotlinOptions {
//        jvmTarget = "11"
//    }
    buildFeatures {
        compose = true
    }
}

kotlin{
    jvmToolchain(21)
}

dependencies {

    implementation(project(":domain"))
    implementation(project(":feature:common"))


    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)

    // --- Compose BOM 기반 ---
    implementation(libs.material)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.bundles.compose)

    // --- Navigation ---
    implementation(libs.bundles.navigation)

    // --- Coil ---
    implementation(libs.coil.compose)

    // --- Hilt ---
    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.navigation.compose)
    ksp(libs.hilt.compiler)

    // --- Serialization ---
    implementation(libs.kotlinx.serialization.json)

    // --- Permissions ---
    implementation(libs.accompanist.permissions)

    // --- Map SDK ---
    implementation(libs.play.services.location)

    // --- Lifecycle & ViewModel Compose ---
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    implementation(libs.androidx.compose.ui.tooling.preview)

    // --- Test ---
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)

    // --- Debug ---
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}