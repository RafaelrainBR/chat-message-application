package com.rafaelrain.message.backend.room.storage

import com.rafaelrain.chatmessage.common.packet.ClientMessageSessionPacket
import com.rafaelrain.message.backend.room.Room
import com.rafaelrain.message.backend.room.connection.PacketClient
import com.rafaelrain.message.backend.room.session.MessageSession

class RoomStorage {
    private val storage = mutableMapOf<String, Room<PacketClient>>()

    fun getRooms(): List<Room<PacketClient>> = storage.values.sortedByDescending { it.createdAt }

    suspend fun addNewSession(session: MessageSession<PacketClient>) {
        return getRoomOrCreate(session.roomName).addNewSession(session)
    }

    suspend fun handleLeave(session: MessageSession<PacketClient>) {
        return getRoomOrCreate(session.roomName).handleLeave(session)
    }

    suspend fun handleClientPacket(
        session: MessageSession<PacketClient>,
        packet: ClientMessageSessionPacket,
    ) {
        return getRoomOrCreate(session.roomName).handleClientPacket(session, packet)
    }

    private fun getRoomOrCreate(name: String): Room<PacketClient> {
        return storage[name]
            ?: Room<PacketClient>(name).also { storage[name] = it }
    }
}
