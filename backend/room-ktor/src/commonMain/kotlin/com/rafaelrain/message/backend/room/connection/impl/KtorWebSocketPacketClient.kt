package com.rafaelrain.message.backend.room.connection.impl

import com.rafaelrain.chatmessage.common.packet.ServerMessageSessionPacket
import com.rafaelrain.message.backend.room.connection.PacketClient
import io.ktor.server.websocket.WebSocketServerSession
import io.ktor.server.websocket.sendSerialized
import io.ktor.websocket.close

class KtorWebSocketPacketClient(
    private val webSocketServerSession: WebSocketServerSession,
) : PacketClient {
    override suspend fun sendSerialized(serverPacket: ServerMessageSessionPacket) {
        return webSocketServerSession.sendSerialized(serverPacket)
    }

    override suspend fun disconnect() {
        webSocketServerSession.close()
    }
}
