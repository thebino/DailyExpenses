plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    kotlin("plugin.serialization")
    id("io.gitlab.arturbosch.detekt") version "1.22.0"
    id("org.jetbrains.kotlinx.kover") version "0.6.1"
    id("com.diffplug.spotless") version "6.13.0"
    id("org.ajoberstar.grgit") version "5.0.0"
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
    implementation("androidx.core:core-ktx:1.9.0")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")


    // dependency injection
    implementation("io.insert-koin:koin-android:3.3.2")
    implementation("io.insert-koin:koin-androidx-compose:3.4.1")
    implementation("io.insert-koin:koin-androidx-workmanager:3.3.2")
    testImplementation("io.insert-koin:koin-test:3.2.2")
    testImplementation("io.insert-koin:koin-test-junit4:3.2.2")


    // Work
    implementation("androidx.work:work-runtime-ktx:2.7.1")


    // Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")


    // Logging
    implementation("com.jakewharton.timber:timber:5.0.1")


    // Compose
    implementation("androidx.activity:activity-compose:1.6.1")
    // https://developer.android.com/jetpack/androidx/releases/compose#2022.11.00
    implementation(platform("androidx.compose:compose-bom:2022.11.00"))
    // Material Design 3
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material:1.3.1")
    // Android Studio Preview support
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")
    // UI Tests
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    implementation("androidx.compose.material:material-icons-core")
    implementation("androidx.compose.material:material-icons-extended")


    // Constraint Layout
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.1")

    implementation("androidx.activity:activity-compose:1.6.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.5.1")


    // Persistence
    implementation("androidx.datastore:datastore-preferences:1.0.0")
    implementation("androidx.room:room-runtime:2.5.0")
    implementation("androidx.room:room-ktx:2.5.0")
    kapt("androidx.room:room-compiler:2.5.0")
    androidTestImplementation("androidx.room:room-testing:2.5.0")


    // ktor
    implementation("io.ktor:ktor-client-core:2.2.1")
    implementation("io.ktor:ktor-client-android:2.2.1")
    implementation("io.ktor:ktor-client-auth:2.2.1")
    implementation("io.ktor:ktor-client-logging:2.2.1")
    implementation("io.ktor:ktor-client-serialization:2.2.1")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.2.1")
    implementation("io.ktor:ktor-client-content-negotiation:2.2.1")
    testImplementation("io.ktor:ktor-client-mock-jvm:2.2.1")


    // Accompanist
    implementation("com.google.accompanist:accompanist-flowlayout:0.27.1")


    // Testing
    testImplementation(kotlin("test"))
    testImplementation("junit:junit:4.13.2")
    testImplementation("androidx.test.ext:junit-ktx:1.1.5")
    testImplementation("io.mockk:mockk:1.13.2")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4")


    // Instrumented testing
    androidTestImplementation(kotlin("test"))
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.3.3")
}

kapt {
    correctErrorTypes = true
}
