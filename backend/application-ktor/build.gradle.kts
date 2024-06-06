plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    jvm()
    linuxX64 {
        binaries {
            executable {
                entryPoint = "com.rafaelrain.message.backend.application.ktor.main"
            }
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.common)
            implementation(projects.backend.room)
            implementation(projects.backend.roomKtor)
            implementation(libs.arrow.core)
            implementation(libs.kotlinx.datetime)
            implementation(libs.ktor.server.core)
            implementation(libs.ktor.server.cio)
            implementation(libs.ktor.server.content.negotiation)
            implementation(libs.ktor.server.websockets)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.koin.core)
            implementation(libs.koin.ktor)
        }
    }
}
