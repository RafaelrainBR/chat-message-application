package com.rafaelrain.chatmessage.common.packet

import kotlinx.serialization.Serializable

@Serializable
data class ServerMessageSessionPacket(
    val type: ServerMessageSessionPacketType,
    val roomName: String,
    val senderName: String,
    val message: String? = null,
    val sentAt: String? = null,
)

@Serializable
enum class ServerMessageSessionPacketType { JOINED, LEAVE, MESSAGE }
