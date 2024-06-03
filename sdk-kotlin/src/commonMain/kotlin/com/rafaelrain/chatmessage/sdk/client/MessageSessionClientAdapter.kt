package com.rafaelrain.chatmessage.sdk.client

import com.rafaelrain.chatmessage.common.packet.ServerMessageSessionPacket
import com.rafaelrain.chatmessage.sdk.session.MessageSession
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.header
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class MessageSessionClientAdapter(
    private val httpClient: HttpClient,
) : MessageSessionClient {
    override fun createSession(
        createSessionRequest: CreateSessionRequest,
        coroutineScope: CoroutineScope,
    ): MessageSession {
        val session = MessageSession(name = createSessionRequest.name, roomName = createSessionRequest.roomName)

        coroutineScope.launch {
            val websocketSession =
                httpClient.webSocketSession(host = "0.tcp.sa.ngrok.io", port = 15345, path = "/messages") {
                    header("Name", createSessionRequest.name)
                    header("Room", createSessionRequest.roomName)
                }

            launch {
                session.listenClientPackets {
                    websocketSession.send(Frame.Text(Json.encodeToString(it)))
                }
            }.start()

            launch {
                websocketSession.incoming.receiveAsFlow().onEach {
                    if (it is Frame.Text) {
                        val packet = Json.decodeFromString<ServerMessageSessionPacket>(it.readText())
                        session.emitServerPacket(packet)
                    }
                }.collect()
            }.start()
        }

        return session
    }
}
