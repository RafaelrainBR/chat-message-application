package com.rafaelrain.chatmessage.sdk.client

import kotlinx.serialization.Serializable

interface RoomsClient {
    suspend fun getRooms(): List<Room>
}

@Serializable
data class Room(
    val name: String,
    val length: Int,
    val users: List<String>,
    val createdAt: String?,
)
