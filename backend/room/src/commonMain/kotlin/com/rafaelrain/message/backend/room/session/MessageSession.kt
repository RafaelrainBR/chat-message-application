package com.rafaelrain.message.backend.room.session

import com.rafaelrain.chatmessage.common.packet.ServerMessageSessionPacket
import com.rafaelrain.message.backend.room.connection.PacketClient
import kotlinx.serialization.Transient

data class MessageSession<out C : PacketClient>(
    val name: String,
    val roomName: String,
    @Transient
    val packetClient: C,
) {
    suspend fun sendPacket(packet: ServerMessageSessionPacket) {
        packetClient.sendSerialized(packet)
    }

    suspend fun disconnect() {
        packetClient.disconnect()
    }

    companion object
}
