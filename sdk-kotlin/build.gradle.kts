plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.kotlin.serialization)
//    alias(libs.plugins.android.application)
}

kotlin {
//    androidTarget()

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
            implementation(libs.ktor.client.cio)
        }
    }
}

// android {
//    namespace = "com.rafaelrain.chatmessage.clientcompose"
//    compileSdk = 34
// }
