package com.rafaelrain.message.backend.application.ktor

import com.rafaelrain.message.backend.application.ktor.plugins.configureKoin
import com.rafaelrain.message.backend.application.ktor.plugins.configureSerialization
import com.rafaelrain.message.backend.application.ktor.plugins.configureSockets
import io.ktor.server.application.Application
import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer

fun main() {
    embeddedServer(CIO, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureSerialization()
    configureKoin()
    configureSockets()
}
