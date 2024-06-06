package com.rafaelrain.message.backend.room

import com.rafaelrain.chatmessage.common.extensions.now
import com.rafaelrain.chatmessage.common.packet.ClientMessageSessionPacket
import com.rafaelrain.chatmessage.common.packet.ClientMessageSessionPacketType
import com.rafaelrain.chatmessage.common.packet.ServerMessageSessionPacket
import com.rafaelrain.chatmessage.common.packet.ServerMessageSessionPacketType
import com.rafaelrain.message.backend.room.connection.PacketClient
import com.rafaelrain.message.backend.room.message.ChatMessage
import com.rafaelrain.message.backend.room.session.MessageSession
import kotlinx.datetime.LocalDateTime

data class Room<C : PacketClient>(
    val name: String,
    val sessions: MutableMap<String, MessageSession<C>> = mutableMapOf(),
    val messages: MutableList<ChatMessage> = mutableListOf(),
    val createdAt: LocalDateTime = LocalDateTime.now(),
) {
    suspend fun addNewSession(session: MessageSession<C>) {
        sessions.remove(session.name)
        sessions[session.name] = session

        notifyJoin(session)
        sendMessageHistory(session)
    }

    suspend fun handleLeave(session: MessageSession<C>) {
        sessions.remove(session.name)

        broadcastPacket(
            ServerMessageSessionPacket.leave(roomName = this.name, sessionName = session.name),
        )
    }

    suspend fun handleClientPacket(
        session: MessageSession<C>,
        packet: ClientMessageSessionPacket,
    ) {
        when (packet.type) {
            ClientMessageSessionPacketType.MESSAGE -> handleMessage(session, packet.message.orEmpty())
        }
    }

    private suspend fun notifyJoin(joinedUser: MessageSession<C>) {
        val packet =
            ServerMessageSessionPacket(
                type = ServerMessageSessionPacketType.JOINED,
                roomName = this.name,
                senderName = joinedUser.name,
            )
        broadcastPacket(packet)
    }

    private suspend fun sendMessageHistory(session: MessageSession<C>) {
        this.messages.asSequence()
            .sortedBy { message -> message.sentAt }
            .map { message -> message.toServerPacket() }
            .forEach { packet -> sendPacketOrDisconnect(session, packet) }
    }

    private suspend fun broadcastPacket(packet: ServerMessageSessionPacket) {
        sessions.forEach { (_, session) ->
            sendPacketOrDisconnect(session, packet)
        }
    }

    private suspend fun sendPacketOrDisconnect(
        session: MessageSession<C>,
        packet: ServerMessageSessionPacket,
    ) {
        runCatching {
            session.sendPacket(packet)
        }.onFailure {
            handleLeave(session)
            runCatching { session.disconnect() }
        }
    }

    private suspend fun handleMessage(
        session: MessageSession<C>,
        message: String,
    ) {
        val chatMessage = ChatMessage(sender = session.name, message = message, roomName = this.name)

        broadcastPacket(chatMessage.toServerPacket())
        messages.add(chatMessage)
    }
}
