plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.ksp)
}
android {
    namespace = "com.sesac.data"
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

    implementation(libs.bundles.compose)
    implementation(libs.retrofit.core)
    implementation(libs.okhttp.urlconnection)
    implementation(libs.androidx.datastore.core)
    implementation(libs.androidx.datastore.preferences.core)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.converter.moshi)
    implementation(libs.moshi.kotlin)
    ksp(libs.moshi.kotlin.codegen)
    implementation(libs.okhttp.core)
    implementation(libs.okhttp.logging.interceptor)
    implementation(libs.kotilinx.coroutines.core)
    implementation(libs.hilt.android)
    implementation(libs.androidx.room.runtime)
    implementation(libs.threetenabp)

    ksp(libs.androidx.room.compiler)
    ksp(libs.hilt.compiler)
}
