import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.room)
//    alias(libs.plugins.screenshot)
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class) compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    jvm("desktop")

    // TODO: enable iOS
//    listOf(
//        iosX64(),
//        iosArm64(),
//        iosSimulatorArm64()
//    ).forEach { iosTarget ->
//        iosTarget.binaries.framework {
//            baseName = "ComposeApp"
//            isStatic = true
//        }
//    }

    sourceSets {
        commonMain.dependencies {}
        commonTest.dependencies {}

        val desktopMain by getting
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
        }

        androidMain.dependencies {
            implementation(projects.frontend.shared)

            // core
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
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

            // Material Design
            implementation(libs.compose.material3)

            // Android Studio Preview support
            implementation(libs.compose.tooling.preview)

            implementation(libs.compose.material.icons.core)
            implementation(libs.compose.material.icons.extended)

            // Constraint Layout
            implementation(libs.androidx.constraintlayout.compose)
            implementation(libs.androidx.lifecycle.viewmodel.compose)

            // Persistence
            implementation(libs.androidx.datastore.preferences)
            implementation(libs.androidx.room.runtime)
            implementation(libs.androidx.room.ktx)

            // ktor
            implementation(libs.bundles.ktor)

            implementation("com.google.accompanist:accompanist-systemuicontroller:0.31.3-beta")
        }

        // androidUnitTest.dependencies doesn't exist
        val androidUnitTest by getting {
            dependencies {
                implementation(libs.kotlin.test)
                implementation(libs.junit)
                implementation(libs.androidx.test.ext.junit.ktx)
                implementation(libs.mockk)
                implementation(libs.jetbrains.kotlinx.coroutines.test)

                // ktor
                implementation(libs.ktor.mock.jvm)
            }
        }

        val androidInstrumentedTest by getting {
            dependencies {
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class) implementation(
                    compose.uiTest
                )
            }
        }
    }
}

android {
    namespace = "pro.stuermer.dailyexpenses"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        applicationId = "pro.stuermer.dailyexpenses"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 15
        versionName = "1.1.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    @Suppress("UnstableApiUsage") experimentalProperties["android.experimental.enableScreenshotTest"] =
        true
    dependencies {
        debugImplementation(compose.uiTooling)
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "pro.stuermer.splitkeyboard"
            packageVersion = "1.0.0"
        }
    }
}

room {
    // composeApp/schemas
    schemaDirectory("$projectDir/schemas")
}
