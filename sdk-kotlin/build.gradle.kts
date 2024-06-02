plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.kotlin.serialization)
    application
}

kotlin {
    jvm()

    sourceSets {
        commonMain.dependencies {
            implementation(project(":common"))
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.websockets)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.kotlinx.datetime)
            implementation(libs.arrow.core)
            implementation(libs.ktx.serialization.json)
        }

        jvmMain.dependencies {
            implementation(libs.ktor.client.cio)
        }
    }
}

application {
    mainClass.set("MainKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}
