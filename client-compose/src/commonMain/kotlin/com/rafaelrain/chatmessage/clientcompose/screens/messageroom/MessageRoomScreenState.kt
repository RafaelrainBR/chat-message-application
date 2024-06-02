package com.rafaelrain.chatmessage.clientcompose.screens.messageroom

import com.rafaelrain.chatmessage.sdk.session.MessageSession

data class MessageRoomScreenState(
    val isConnected: Boolean = false,
    val name: String = "",
    val roomName: String = "",
    val message: String = "",
    val messages: List<Message> = emptyList(),
    val messageSession: MessageSession? = null,
)
