plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.secrets.gradle.plugin)  // secret 설정 시 해당 내용 추가
}

android {
    namespace = "com.sesac.android7hours"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.naver.maps.map.demo" // 여기 부분이 네이버 등록된 패키지명이 같아야 함
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

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
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
//    kotlinOptions {
//        jvmTarget = "11"
//    }
    kotlin{
        jvmToolchain(21)
    }
    buildFeatures {
        compose = true
        buildConfig = true // secret 설정 시 해당 내용 추가
    }
    composeOptions {
        // Compose Compiler 버전 지정 (현재 사용하는 Compose UI 버전에 맞춰야 함)
        kotlinCompilerExtensionVersion = "1.5.11"
    }
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

    implementation(project(":feature:home"))
    implementation(project(":feature:community"))
    implementation(project(":feature:common"))
    implementation(project(":feature:monitor"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    implementation("com.naver.maps:map-sdk:3.23.0")  // 해당 부분 넣어야됨
    implementation("com.google.android.gms:play-services-location:21.3.0")
    implementation("com.jakewharton.threetenabp:threetenabp:1.4.6") // localDate 사용하려면 sdk 26 이하에서는 이렇게 써야됨
}