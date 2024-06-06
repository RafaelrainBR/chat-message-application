plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    jvm()
    linuxX64()

    sourceSets {
        commonMain.dependencies {
            implementation(projects.common)
            implementation(libs.ktx.serialization.json)
            implementation(libs.kotlinx.datetime)
        }
    }
}
