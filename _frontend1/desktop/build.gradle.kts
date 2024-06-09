import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
//    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.jetbrains.kotlin.jvm)
    alias(libs.plugins.kover)
}

//kotlin {
//    androidTarget {
//        @OptIn(ExperimentalKotlinGradlePluginApi::class)
//        compilerOptions {
//            jvmTarget.set(JvmTarget.JVM_11)
//        }
//    }

//    jvm("desktop")

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

//    sourceSets {
//        commonMain.dependencies {
//            implementation(compose.runtime)
//            implementation(compose.foundation)
//            implementation(compose.material)
//            implementation(compose.ui)
//            implementation(compose.components.resources)
//            implementation(compose.components.uiToolingPreview)
//            implementation(projects.common)
//
//            implementation(libs.androidx.room.runtime)
//        }
//        commonTest.dependencies {}
//
//        val desktopMain by getting
//        desktopMain.dependencies {
//            implementation(compose.desktop.currentOs)
//        }

//        androidMain.dependencies {
//            // core
//            implementation(compose.preview)
//            implementation(libs.androidx.activity.compose)
//            implementation(libs.androidx.core.ktx)
//
//            // Coroutines
//            implementation(libs.jetbrains.kotlinx.coroutines.core)
//            implementation(libs.jetbrains.kotlinx.coroutines.android)
//
//            // dependency injection
//            implementation(libs.bundles.koin)
//
//            // Work
//            implementation(libs.androidx.work.runtime.ktx)
//
//            // Serialization
//            implementation(libs.jetbrains.kotlinx.serialization.json)
//
//            // Logging
//            implementation(libs.com.jakewharton.timber)
//
//            // Compose
//            implementation(libs.androidx.activity.compose)
//
//            // Material Design
//            implementation(libs.compose.material3)
//
//            // Android Studio Preview support
//            implementation(libs.compose.tooling.preview)
//
//            implementation(libs.compose.material.icons.core)
//            implementation(libs.compose.material.icons.extended)
//
//            // Constraint Layout
//            implementation(libs.androidx.constraintlayout.compose)
//            implementation(libs.androidx.lifecycle.viewmodel.compose)
//
//            // Persistence
//            implementation(libs.androidx.datastore.preferences)
//            implementation(libs.androidx.room.runtime)
//            implementation(libs.androidx.room.ktx)
//
//            // ktor
//            implementation(libs.bundles.ktor)
//        }
//
//        // androidUnitTest.dependencies doesn't exist
//        val androidUnitTest by getting {
//            dependencies {
//                implementation(libs.kotlin.test)
//                implementation(libs.junit)
//                implementation(libs.androidx.test.ext.junit.ktx)
//                implementation(libs.mockk)
//                implementation(libs.jetbrains.kotlinx.coroutines.test)
//
//                // ktor
//                implementation(libs.ktor.mock.jvm)
//            }
//        }

//        val androidInstrumentedTest by getting {
//            dependencies {
//                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
//                implementation(compose.uiTest)
//            }
//        }
//    }
//}

dependencies {
    "kover"(project(":common"))

    implementation(compose.desktop.currentOs)

    // Include the Test API
    testImplementation(compose.desktop.uiTestJUnit4)
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "pro.stuermer.dailyexpenses"
            packageVersion = "1.0.0"
        }
    }
}

kover {
    currentProject {
        createVariant("custom") {
            add("jvm")
            addWithDependencies("debug")
        }
    }
    reports {
        // filters for all report types of all build variants
        filters {
            excludes {
                androidGeneratedClasses()
            }
        }

        variant("release") {
            // filters for all report types only of 'release' build type
            filters {
                excludes {
                    androidGeneratedClasses()
                    classes(
                        // excludes debug classes
                        "*.DebugUtil"
                    )
                }
            }
        }
    }
}
