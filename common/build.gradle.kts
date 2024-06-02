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
            implementation(libs.kotlinx.datetime)
            implementation(libs.arrow.core)
            implementation(libs.ktx.serialization.json)
        }
    }
}

// android {
//    namespace = "com.rafaelrain.chatmessage.clientcompose"
//    compileSdk = 34
// }
