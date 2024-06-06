package com.rafaelrain.message.backend.application.ktor.plugins

import com.rafaelrain.message.backend.application.ktor.routes.messageRoutes
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.routing.routing
import io.ktor.server.websocket.WebSockets
import kotlinx.serialization.json.Json

private const val PING_PERIOD_MILLIS = 15_000L
private const val TIMEOUT_MILLIS = 15_000L

fun Application.configureSockets() {
    install(WebSockets) {
        pingPeriodMillis = PING_PERIOD_MILLIS
        timeoutMillis = TIMEOUT_MILLIS
        maxFrameSize = Long.MAX_VALUE
        masking = false

        contentConverter = KotlinxWebsocketSerializationConverter(Json)
    }

    routing {
        messageRoutes()
    }
}
