package com.rafaelrain.message.backend.room.connection

import com.rafaelrain.chatmessage.common.packet.ServerMessageSessionPacket

interface PacketClient {
    suspend fun sendSerialized(serverPacket: ServerMessageSessionPacket)

    suspend fun disconnect()
}
