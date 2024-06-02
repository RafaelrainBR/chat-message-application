package com.rafaelrain.chatmessage.sdk.client

import com.rafaelrain.chatmessage.sdk.session.MessageSession
import kotlinx.coroutines.CoroutineScope
import kotlinx.serialization.Serializable

fun interface MessageSessionClient {
    fun createSession(
        createSessionRequest: CreateSessionRequest,
        coroutineScope: CoroutineScope,
    ): MessageSession
}

@Serializable
data class CreateSessionRequest(
    val name: String,
    val roomName: String,
)
