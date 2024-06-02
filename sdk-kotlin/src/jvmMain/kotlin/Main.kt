import com.rafaelrain.chatmessage.common.packet.ClientMessageSessionPacket
import com.rafaelrain.chatmessage.common.packet.ClientMessageSessionPacketType
import com.rafaelrain.chatmessage.sdk.client.CreateSessionRequest
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.Executors
import kotlin.time.Duration.Companion.seconds

suspend fun main() {
    val sdk = ChatMessageSdk.create()

    val coroutineScope =
        CoroutineScope(Executors.newFixedThreadPool(6).asCoroutineDispatcher() + SupervisorJob() + exceptionHandler)

    val session =
        sdk.messageSessionClient.createSession(CreateSessionRequest(name = "Sdk", roomName = "sala1"), coroutineScope)

    coroutineScope.launch {
        session.listenServerPackets {
            println("Received $it")
        }
    }

//    while (true) {
//        readlnOrNull().let { text ->
//            session.emitClientPacket(
//                ClientMessageSessionPacket(
//                    type = ClientMessageSessionPacketType.MESSAGE,
//                    roomName = session.roomName,
//                    senderName = session.name,
//                    message = text,
//                ),
//            )
//        }
//    }

    coroutineScope.launch {
        delay(5.seconds)
        println("Passou 5 segundos")
        repeat(3) {
            session.emitClientPacket(
                ClientMessageSessionPacket(
                    type = ClientMessageSessionPacketType.MESSAGE,
                    roomName = session.roomName,
                    senderName = session.name,
                    message = "Mensagem $it",
                ),
            )
        }
    }
    while (true) {
    }
}

val exceptionHandler =
    CoroutineExceptionHandler { ctx, throwable ->
        throwable.printStackTrace()
    }
