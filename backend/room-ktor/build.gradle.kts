plugins {
    alias(libs.plugins.multiplatform)
}

kotlin {
    jvm()
    linuxX64()

    sourceSets {
        commonMain.dependencies {
            implementation(projects.common)
            implementation(projects.backend.room)
            implementation(libs.ktor.server.websockets)
        }
    }
}
