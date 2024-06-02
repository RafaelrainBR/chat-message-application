package com.rafaelrain.chatmessage.common.packet

import kotlinx.serialization.Serializable

@Serializable
data class ClientMessageSessionPacket(
    val type: ClientMessageSessionPacketType,
    val roomName: String,
    val senderName: String,
    val message: String? = null,
)

@Serializable
enum class ClientMessageSessionPacketType { MESSAGE, }
