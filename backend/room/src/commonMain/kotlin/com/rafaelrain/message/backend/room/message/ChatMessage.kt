package com.rafaelrain.message.backend.room.message

import com.rafaelrain.chatmessage.common.extensions.now
import com.rafaelrain.chatmessage.common.packet.ServerMessageSessionPacket
import kotlinx.datetime.LocalDateTime

data class ChatMessage(
    val sender: String,
    val roomName: String,
    val message: String,
    val sentAt: LocalDateTime = LocalDateTime.now(),
) {
    fun toServerPacket(): ServerMessageSessionPacket {
        return ServerMessageSessionPacket.message(
            senderName = sender,
            roomName = roomName,
            message = message,
            sentAt = sentAt,
        )
    }
}
