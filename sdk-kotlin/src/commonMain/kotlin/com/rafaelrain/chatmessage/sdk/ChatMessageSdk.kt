package com.rafaelrain.chatmessage.sdk

import com.rafaelrain.chatmessage.sdk.client.MessageSessionClient
import com.rafaelrain.chatmessage.sdk.client.MessageSessionClientAdapter
import com.rafaelrain.chatmessage.sdk.client.RoomsClient
import com.rafaelrain.chatmessage.sdk.client.RoomsClientAdapter
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class ChatMessageSdk private constructor(
    val messageSessionClient: MessageSessionClient,
    val roomsClient: RoomsClient,
) {
    companion object {
        fun create(
            apiUrl: String,
            webSocketHost: String,
            webSocketPort: Int,
            urlProtocol: URLProtocol = URLProtocol.WSS,
        ): ChatMessageSdk {
            val httpClient = createHttpClient()

            val messageSessionClient =
                MessageSessionClientAdapter(
                    httpClient = httpClient,
                    urlProtocol = urlProtocol,
                    host = webSocketHost,
                    port = webSocketPort,
                )

            val roomsClient =
                RoomsClientAdapter(
                    httpClient = httpClient,
                    baseUrl = apiUrl,
                )
            return ChatMessageSdk(messageSessionClient, roomsClient)
        }

        private fun createHttpClient(): HttpClient {
            return HttpClient {
                install(ContentNegotiation) {
                    json()
                }
                install(WebSockets) {
                    contentConverter = KotlinxWebsocketSerializationConverter(Json)
                }
            }
        }
    }
}
