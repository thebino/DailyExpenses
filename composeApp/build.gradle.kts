import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
    alias(libs.plugins.detekt)
    alias(libs.plugins.kover)
//    alias(libs.plugins.screenshot)
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    jvm("desktop")
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }
    
    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(projects.shared)

            implementation(libs.androidx.room.runtime)
        }
        commonTest.dependencies {

        }

        val desktopMain by getting
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
        }

        androidMain.dependencies {
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
            implementation(libs.compose.material)
            implementation(libs.compose.material3)

            // Android Studio Preview support
            implementation(libs.compose.tooling.preview)
//             debugImplementation(libs.compose.tooling)

            // UI Tests
//            androidTestImplementation(libs.compose.ui.test.junit4)
//            debugImplementation(libs.compose.ui.test.manifest)

            implementation(libs.compose.material.icons.core)
            implementation(libs.compose.material.icons.extended)


            // Constraint Layout
//            implementation(libs.androidx.constraintlayout)
            implementation(libs.androidx.constraintlayout.compose)
            implementation(libs.androidx.lifecycle.viewmodel.compose)



            // Persistence
            implementation(libs.androidx.datastore.preferences)
            implementation(libs.androidx.room.runtime)
            implementation(libs.androidx.room.ktx)
//            kapt(libs.androidx.room.compiler)
//            testImplementation(libs.androidx.room.testing)


            // ktor
            implementation(libs.bundles.ktor)



            // Accompanist
//            implementation(libs.bundles.accompanist)


            // Testing


            // Instrumented testing
//            androidTestImplementation(libs.kotlin.test)
//            androidTestImplementation(libs.androidx.test.ext.junit)
//            androidTestImplementation(libs.androidx.test.espresso.core)
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
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.uiTest)
//                implementation(libs.compose.ui.test.junit4)
//                implementation(libs.compose.ui.test.manifest)
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
    @Suppress("UnstableApiUsage")
    experimentalProperties["android.experimental.enableScreenshotTest"] = true
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

//dependencies {
//    add("kspAndroid", libs.androidx.room.compiler)
//    add("kspIosSimulatorArm64", libs.androidx.room.compiler)
//    add("kspIosX64", libs.androidx.room.compiler)
//    add("kspIosArm64", libs.androidx.room.compiler)
//}

room {
    // composeApp/schemas
    schemaDirectory("$projectDir/schemas")
}