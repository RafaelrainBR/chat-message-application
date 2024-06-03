package com.rafaelrain.chatmessage.sdk.client

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class RoomsClientAdapter(
    private val httpClient: HttpClient,
    private val baseUrl: String,
) : RoomsClient {
    override suspend fun getRooms(): List<Room> {
        return httpClient.get("$baseUrl/rooms").body()
    }
}
