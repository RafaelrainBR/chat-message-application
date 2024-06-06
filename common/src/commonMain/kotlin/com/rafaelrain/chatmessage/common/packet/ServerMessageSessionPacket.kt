package com.rafaelrain.chatmessage.common.packet

import com.rafaelrain.chatmessage.common.extensions.now
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class ServerMessageSessionPacket(
    val type: ServerMessageSessionPacketType,
    val roomName: String,
    val senderName: String,
    val message: String? = null,
    val sentAt: LocalDateTime? = null,
) {
    companion object {
        fun message(
            roomName: String,
            senderName: String,
            message: String,
            sentAt: LocalDateTime = LocalDateTime.now(),
        ): ServerMessageSessionPacket {
            return ServerMessageSessionPacket(
                type = ServerMessageSessionPacketType.MESSAGE,
                roomName = roomName,
                senderName = senderName,
                message = message,
                sentAt = sentAt,
            )
        }

        fun leave(
            roomName: String,
            sessionName: String,
            sentAt: LocalDateTime = LocalDateTime.now(),
        ): ServerMessageSessionPacket {
            return ServerMessageSessionPacket(
                ServerMessageSessionPacketType.LEAVE,
                roomName = roomName,
                senderName = sessionName,
                sentAt = sentAt,
            )
        }
    }
}

@Serializable
enum class ServerMessageSessionPacketType { JOINED, LEAVE, MESSAGE }
