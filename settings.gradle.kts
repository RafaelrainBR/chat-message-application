enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
rootProject.name = "chat-message-application"

pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        mavenLocal()
    }
}

include(":backend:application-ktor")
include(":backend:application-ktor-java")
include(":backend:room")
include(":backend:room-ktor")
include(":client-compose")
include(":common")
include(":sdk-kotlin")
