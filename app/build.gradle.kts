plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.detekt)
    alias(libs.plugins.kover)
    alias(libs.plugins.spotless)
    alias(libs.plugins.grgit)
}

android {
    namespace = "pro.stuermer.dailyexpenses"
    compileSdk = 33

    defaultConfig {
        // API 26 | 8.0 java 8 time api
        minSdk = 26
        targetSdk = 33
        versionCode = grgit.log().size
        versionName = "1.1.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "API_URL", "\"https://app.stuermer.pro\"")
        buildConfigField("String", "CLIENT_CERT_FILE", "\"app.cert.p12\"")
        buildConfigField("String", "CLIENT_CERT_PASSWORD", "\"hJEkrrrVEC67RwDLD\"")

        // export directory for room schema
        javaCompileOptions {
            annotationProcessorOptions {
                arguments(
                    mapOf(
                        "room.schemaLocation" to "$projectDir/schemas",
                        "room.incremental" to "true",
                        "room.expandProjection" to "true"
                    )
                )
            }
        }
    }

    signingConfigs {
        create("release") {
            // Play App Signing is used
        }
    }

    buildTypes {
        debug {
            versionNameSuffix = "-(${grgit.head().abbreviatedId})"
            applicationIdSuffix = ".debug"
            buildConfigField("String", "API_URL", "\"http://172.22.11.226:8080\"")
        }
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("release")
        }
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.3.2"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
        freeCompilerArgs = freeCompilerArgs + "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi"
        freeCompilerArgs = freeCompilerArgs + "-opt-in=androidx.compose.ui.ExperimentalComposeUiApi"
        freeCompilerArgs = freeCompilerArgs + "-opt-in=androidx.compose.foundation.ExperimentalFoundationApi"
        freeCompilerArgs = freeCompilerArgs + "-opt-in=androidx.compose.foundation.layout.ExperimentalLayoutApi"
        freeCompilerArgs = freeCompilerArgs + "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api"
        freeCompilerArgs = freeCompilerArgs + "-opt-in=androidx.compose.material.ExperimentalMaterialApi"
        freeCompilerArgs = freeCompilerArgs + "-opt-in=kotlinx.serialization.ExperimentalSerializationApi"
    }

    buildFeatures {
        compose = true
    }
}

detekt {
    config = files("../detekt.yml")
}

kover {
    htmlReport {
        // run koverHtmlReport task `check`
        onCheck.set(true)
    }
    xmlReport {
        // run koverMergedXmlReport task `check`
        onCheck.set(true)
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)

    // Coroutines
    implementation(libs.jetbrains.kotlinx.coroutines.core)
    implementation(libs.jetbrains.kotlinx.coroutines.android)


    // dependency injection
    implementation(libs.bundles.koin)


    // Work
    implementation(libs.androidx.work.runtime.ktx)


    // Serialization
    implementation(libs.jetbrains.kotlinx.serialization.json)


    // Logging
    implementation(libs.com.jakewharton.timber)


    // Compose
    implementation(libs.androidx.activity.compose)
    // https://developer.android.com/jetpack/androidx/releases/compose#2022.11.00
    implementation(platform(libs.compose.bom))
    androidTestImplementation(platform(libs.compose.bom))
    // Material Design
    implementation(libs.compose.material)
    implementation(libs.compose.material3)
    // Android Studio Preview support
    implementation(libs.compose.tooling.preview)
    debugImplementation(libs.compose.tooling)
    // UI Tests
    androidTestImplementation(libs.compose.ui.test.junit4)
    debugImplementation(libs.compose.ui.test.manifest)

    implementation(libs.compose.material.icons.core)
    implementation(libs.compose.material.icons.extended)


    // Constraint Layout
    implementation(libs.androidx.constraintlayout.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)



    // Persistence
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    kapt(libs.androidx.room.compiler)
    testImplementation(libs.androidx.room.testing)


    // ktor
    implementation(libs.bundles.ktor)
    testImplementation(libs.ktor.mock.jvm)


    // Accompanist
    implementation(libs.bundles.accompanist)


    // Testing
    testImplementation(libs.kotlin.test)
    testImplementation(libs.junit)
    testImplementation(libs.androidx.test.ext.junit.ktx)
    testImplementation(libs.mockk)
    testImplementation(libs.jetbrains.kotlinx.coroutines.test)


    // Instrumented testing
    androidTestImplementation(libs.kotlin.test)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
}

kapt {
    correctErrorTypes = true
}
