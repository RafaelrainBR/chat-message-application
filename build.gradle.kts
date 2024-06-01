import io.gitlab.arturbosch.detekt.Detekt

plugins {
    // Common
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.multiplatform) apply false

    // Backend
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.ktor) apply false

    // Compose
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.compose) apply false
    alias(libs.plugins.android.application) apply false

    alias(libs.plugins.ktlint)
    alias(libs.plugins.detekt)
}

allprojects {
    group = "com.rafaelrain.chatmessage"
    version = "1.0-SNAPSHOT"

    apply(plugin = rootProject.libs.plugins.ktlint.get().pluginId)
    apply(plugin = rootProject.libs.plugins.detekt.get().pluginId)

    detekt {
        buildUponDefaultConfig = true
        baseline = file("$rootDir/config/detekt/baseline.xml")
    }

    tasks.withType<Detekt>().configureEach {
        jvmTarget = "17"
        reports {
            html.required = true
            md.required = true
        }
        basePath = rootDir.absolutePath
    }
}
