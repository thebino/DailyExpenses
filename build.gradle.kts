import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask

plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.jetbrains.compose) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.skie) apply false
    alias(libs.plugins.detekt)
    alias(libs.plugins.kover)
}

kover {
    reports {
        filters {
            includes {
                classes("pro.stuermer.dailyexpenses.*")
            }
        }
        total {}
        verify {}
    }
}

dependencies {
    detektPlugins(libs.detekt.formatting)
}

detekt {
    autoCorrect = true
    buildUponDefaultConfig = true
    config.setFrom("${project.rootDir}/detekt.yml")
}

tasks.withType<DetektCreateBaselineTask>().configureEach {
    jvmTarget = JavaVersion.VERSION_1_8.toString()
}

tasks.withType<Detekt>().configureEach {
    jvmTarget = JavaVersion.VERSION_1_8.toString()
    reports {
        html.required.set(true)
        md.required.set(true)
        sarif.required.set(false)
        txt.required.set(false)
        xml.required.set(true)
    }
}

val detektAll by tasks.registering(Detekt::class) {
    description = "Run detekt analysis on entire project"
    parallel = true
    buildUponDefaultConfig = true
    config.setFrom("${project.rootDir}/detekt.yml")
    setSource(files(projectDir))
    include("**/*.kt", "**/*.kts")
    exclude("**/generated/", "**/test/", "**/jvmTest", "**/commonTest", "resources/", "*/build/*")
}
