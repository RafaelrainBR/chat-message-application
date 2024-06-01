package com.rafaelrain.chatmessage.backend.plugins

import com.rafaelrain.chatmessage.backend.routes.messageRoutes
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.routing.routing
import io.ktor.server.websocket.WebSockets
import io.ktor.server.websocket.pingPeriod
import io.ktor.server.websocket.timeout
import io.ktor.server.websocket.webSocket
import io.ktor.websocket.CloseReason
import io.ktor.websocket.CloseReason.Codes.NORMAL
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.serialization.json.Json
import java.time.Duration

private const val PING_PERIOD_SECONDS = 15L
private const val TIMEOUT_SECONDS = 15L

fun Application.configureSockets() {
    install(WebSockets) {
        pingPeriod = Duration.ofSeconds(PING_PERIOD_SECONDS)
        timeout = Duration.ofSeconds(TIMEOUT_SECONDS)
        maxFrameSize = Long.MAX_VALUE
        masking = false

        contentConverter = KotlinxWebsocketSerializationConverter(Json)
    }

    routing {
        messageRoutes()
        webSocket("/ws") { // websocketSession
            for (frame in incoming) {
                if (frame is Frame.Text) {
                    val text = frame.readText()
                    outgoing.send(Frame.Text("YOU SAID: $text"))
                    if (text.equals("bye", ignoreCase = true)) {
                        close(CloseReason(NORMAL, "Client said BYE"))
                    }
                }
            }
        }
    }
}
