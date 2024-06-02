plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    jvm()

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.datetime)
            implementation(libs.arrow.core)
            implementation(libs.ktx.serialization.json)
        }
    }
}
