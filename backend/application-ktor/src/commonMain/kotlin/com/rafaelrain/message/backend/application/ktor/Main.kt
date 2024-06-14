package com.rafaelrain.message.backend.application.ktor

import arrow.continuations.SuspendApp
import arrow.continuations.ktor.server
import arrow.fx.coroutines.resourceScope
import com.rafaelrain.message.backend.application.ktor.plugins.configureKoin
import com.rafaelrain.message.backend.application.ktor.plugins.configureSerialization
import com.rafaelrain.message.backend.application.ktor.plugins.configureSockets
import io.ktor.server.application.Application
import io.ktor.server.cio.CIO
import kotlinx.coroutines.awaitCancellation

fun main() =
    SuspendApp {
        resourceScope {
            server(CIO, module = Application::module, port = 8080)
            awaitCancellation()
        }
    }

fun Application.module() {
    configureSerialization()
    configureKoin()
    configureSockets()
}
