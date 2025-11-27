plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.ksp)
    alias(libs.plugins.secrets.gradle.plugin)
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
        buildConfig = true
    }
}

kotlin{
    jvmToolchain(21)
}
secrets {
    // 사용할 프로퍼티 파일이름을 선언(선언하지 않으면기본"local.properties")
    propertiesFileName = "secret.properties"
    // CI/CD 환경을 위한 기본 프로퍼티 파일을 지정
// 이 파일은 버전 관리에 포함될 수 있음
//defaultPropertiesFileName = "secrets.defaults.properties"

// Secrets Plug-In이 무시할 키의 목록을 정규 표현식으로 지정가능
// "sdk.dir"은 기본적으로 무시
//ignoreList.add("debug.*")  : "debug"로 시작하는 모든 키 무시
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
    implementation(libs.moshi.adapters)
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
    implementation("com.google.maps:google-maps-services:2.2.0")
    // --- Map SDK ---
    implementation(libs.map.sdk)
}
