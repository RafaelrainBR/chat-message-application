plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor)
}

dependencies {
    implementation(projects.backend.applicationKtor)
    implementation(libs.logback.classic)
}

application {
    mainClass.set("com.rafaelrain.message.backend.application.ktor.MainKt")
}
