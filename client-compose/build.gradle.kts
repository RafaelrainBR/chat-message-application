import com.android.build.api.dsl.ManagedVirtualDevice
import org.jetbrains.compose.desktop.application.dsl.TargetFormat.AppImage
import org.jetbrains.compose.desktop.application.dsl.TargetFormat.Dmg
import org.jetbrains.compose.desktop.application.dsl.TargetFormat.Exe
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.android.application)
}

val androidVersionCode: String by properties
val androidVersionName: String by properties

kotlin {
    androidTarget {
        compilations.all {
            compileTaskProvider {
                compilerOptions {
                    jvmTarget.set(JvmTarget.JVM_1_8)
                    freeCompilerArgs.add("-Xjdk-release=${JavaVersion.VERSION_1_8}")
                }
            }
        }
    }

    jvm()

//    js {
//        browser()
//        binaries.executable()
//    }

//    https://youtrack.jetbrains.com/issue/KTOR-5587/Ktor-client-for-Kotlin-Wasm
//    @OptIn(ExperimentalWasmDsl::class)
//    wasmJs {
//        browser()
//        binaries.executable()
//    }

    sourceSets {
        all {
            languageSettings {
                optIn("org.jetbrains.compose.resources.ExperimentalResourceApi")
            }
        }
        commonMain.dependencies {
            implementation(project(":common"))
            implementation(project(":sdk-kotlin"))
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.voyager.navigator)
            implementation(libs.voyager.screenModel)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.cio)
            implementation(libs.kotlinx.datetime)
            implementation(libs.androidx.ui.text.google.fonts)
        }

        androidMain.dependencies {
            implementation(compose.uiTooling)
            implementation(libs.androidx.activityCompose)
            implementation(project(":sdk-kotlin"))
        }

        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(project(":sdk-kotlin"))
        }
    }
}

android {
    namespace = "com.rafaelrain.chatmessage.clientcompose"
    compileSdk = 34

    defaultConfig {
        minSdk = 24
        targetSdk = 34

        applicationId = "com.rafaelrain.chatmessage.clientcompose.androidApp"
        versionCode = androidVersionCode.toInt()
        versionName = androidVersionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    sourceSets["main"].apply {
        manifest.srcFile("src/androidMain/AndroidManifest.xml")
        res.srcDirs("src/androidMain/res")
    }

    // https://developer.android.com/studio/test/gradle-managed-devices
    @Suppress("UnstableApiUsage")
    testOptions {
        managedDevices.devices {
            maybeCreate<ManagedVirtualDevice>("pixel5").apply {
                device = "Pixel 5"
                apiLevel = 34
                systemImageSource = "aosp"
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.11"
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(AppImage, Dmg, Exe)
            packageName = "com.rafaelrain.chatmessage.desktopApp"
            packageVersion = "1.0.0"
        }
    }
}
