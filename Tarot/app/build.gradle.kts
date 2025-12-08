plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "com.fourleafclover.tarot"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.fourleafclover.tarot"
        minSdk = 24
        targetSdk = 35
        versionCode = 18
        versionName = "2.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }


    signingConfigs {
        create("release") {
            keyAlias = rootProject.properties["KEY_ALIAS"].toString()
            keyPassword = rootProject.properties["KEY_PASSWORD"].toString()
            storeFile = File(rootProject.properties["STORE_FILE"].toString())
            storePassword = rootProject.properties["STORE_PASSWORD"].toString()
        }
    }

    buildTypes {
        // 개발용
        debug {
            isMinifyEnabled = false
            isDebuggable = true
            manifestPlaceholders["app_name"] = "@string/app_name_dev"
            manifestPlaceholders["app_icon"] = "@mipmap/ic_launcher"
        }

        // 배포용
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
            manifestPlaceholders["app_name"] = "@string/app_name"
            manifestPlaceholders["app_icon"] = "@mipmap/ic_launcher_tarot_for_u"

        }
    }

    flavorDimensions.add("server")
    productFlavors {
        // 개발용
        create("dev") {
            dimension = "server"
            buildConfigField("String", "BASE_URL", "\"https://dev.tarot-for-u.shop\"")
            buildConfigField("String", "SERVER", "\"dev\"")
            versionNameSuffix = "-dev"
        }

        // 배포용
        create("live") {
            dimension = "server"
            buildConfigField("String", "BASE_URL", "\"https://prod.tarot-for-u.shop\"")
            buildConfigField("String", "SERVER", "\"live\"")
            versionNameSuffix = "-live"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

kotlin {
    jvmToolchain(17)
}

dependencies {

    implementation(platform("androidx.compose:compose-bom:2023.05.01"))
    implementation("androidx.activity:activity-compose:1.7.2")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.runtime:runtime")
    implementation("androidx.compose.runtime:runtime-livedata")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:${rootProject.extra["lifecycle_version"]}")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:${rootProject.extra["lifecycle_version"]}")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:${rootProject.extra["lifecycle_version"]}")
    implementation("androidx.lifecycle:lifecycle-viewmodel-savedstate:${rootProject.extra["lifecycle_version"]}")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-compose:${rootProject.extra["lifecycle_version"]}")
    implementation ("androidx.navigation:navigation-compose:2.7.3")
    implementation("androidx.compose.foundation:foundation:1.5.1")
    implementation("androidx.compose.foundation:foundation-android:1.5.1")
    implementation ("androidx.compose.material:material:1.6.2")
    implementation("io.coil-kt:coil-compose:2.5.0")
    implementation("androidx.test:monitor:1.6.1")
    implementation("androidx.test.ext:junit-ktx:1.1.5")

    debugImplementation("androidx.compose.ui:ui-test-manifest")
    debugImplementation("androidx.compose.ui:ui-tooling")

    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")

    //splashscreen api
    implementation ("androidx.core:core-splashscreen:1.0.1")

    // firebase
    implementation(platform("com.google.firebase:firebase-bom:32.7.1"))
    implementation("com.google.firebase:firebase-dynamic-links-ktx")
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-firestore")

    // socket.io
    implementation ("io.socket:socket.io-client:2.1.0") {
        exclude(group = "org.json", module = "json")
    }

    // system ui controller
    implementation ("com.google.accompanist:accompanist-systemuicontroller:0.27.0")

    implementation("com.squareup.okhttp3:okhttp:4.12.0")

    // interceptor
    implementation ("com.squareup.okhttp3:logging-interceptor:4.9.2")

    // hilt
    // Hilt dependencies
    implementation("com.google.dagger:hilt-android:2.51.1")
    ksp("com.google.dagger:hilt-compiler:2.51.1")

    // Hilt navigation for Compose
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
}