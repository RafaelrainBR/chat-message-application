package com.rafaelrain.chatmessage.clientcompose.screens.messageroom

import kotlinx.datetime.LocalDateTime

data class Message(
    val senderName: String,
    val message: String,
    val isFromUser: Boolean = false,
    val sentAt: LocalDateTime?,
)
