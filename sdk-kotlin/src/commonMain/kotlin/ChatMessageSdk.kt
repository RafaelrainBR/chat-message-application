import com.rafaelrain.chatmessage.sdk.client.MessageSessionClient
import com.rafaelrain.chatmessage.sdk.client.MessageSessionClientAdapter
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class ChatMessageSdk private constructor(
    val messageSessionClient: MessageSessionClient,
) {
    companion object {
        fun create(): ChatMessageSdk {
            val messageSessionClient = MessageSessionClientAdapter(httpClient = createHttpClient())

            return ChatMessageSdk(messageSessionClient)
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
