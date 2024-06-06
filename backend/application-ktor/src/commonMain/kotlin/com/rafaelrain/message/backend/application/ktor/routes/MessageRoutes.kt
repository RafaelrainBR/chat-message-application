package com.rafaelrain.message.backend.application.ktor.routes

import arrow.core.Either
import arrow.core.getOrElse
import arrow.core.raise.either
import arrow.core.raise.ensureNotNull
import com.rafaelrain.chatmessage.common.packet.ClientMessageSessionPacket
import com.rafaelrain.message.backend.room.Room
import com.rafaelrain.message.backend.room.connection.PacketClient
import com.rafaelrain.message.backend.room.connection.impl.KtorWebSocketPacketClient
import com.rafaelrain.message.backend.room.session.MessageSession
import com.rafaelrain.message.backend.room.storage.RoomStorage
import io.ktor.server.application.call
import io.ktor.server.request.header
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.websocket.WebSocketServerSession
import io.ktor.server.websocket.webSocket
import io.ktor.websocket.CloseReason
import io.ktor.websocket.CloseReason.Codes.CANNOT_ACCEPT
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.koin.ktor.ext.inject

fun Route.messageRoutes() {
    val roomStorage by inject<RoomStorage>()

    get("/rooms") {
        call.respond(roomStorage.getRooms().map(RoomDTO.Companion::fromRoom))
    }

    webSocket("/messages") {
        val session =
            MessageSession
                .fromWebSocketSession(session = this)
                .getOrElse { errorMessage -> return@webSocket close(CloseReason(CANNOT_ACCEPT, errorMessage)) }

        roomStorage.addNewSession(session)

        runCatching {
            for (frame in incoming) {
                if (frame is Frame.Text) {
                    handleTextFrame(roomStorage, session, frame)
                } else if (frame is Frame.Close) {
                    roomStorage.handleLeave(session)
                }
            }
        }.getOrElse { error ->
            if (error is ClosedReceiveChannelException) {
                roomStorage.handleLeave(session)
            } else {
                throw error
            }
        }

        roomStorage.handleLeave(session)
    }
}

private fun MessageSession.Companion.fromWebSocketSession(
    session: WebSocketServerSession,
): Either<String, MessageSession<KtorWebSocketPacketClient>> {
    return either {
        val userName = ensureNotNull(session.call.request.header("Name")) { "Name header not found" }
        val roomName = ensureNotNull(session.call.request.header("Room")) { "Name header not found" }

        MessageSession(
            packetClient = KtorWebSocketPacketClient(session),
            name = userName,
            roomName = roomName,
        )
    }
}

private suspend fun handleTextFrame(
    roomStorage: RoomStorage,
    session: MessageSession<KtorWebSocketPacketClient>,
    frame: Frame.Text,
) = runCatching {
    val text = frame.readText()
    val packet = Json.decodeFromString<ClientMessageSessionPacket>(text)
    roomStorage.handleClientPacket(session, packet)
}.onFailure { it.printStackTrace() }

@Serializable
data class RoomDTO(
    val name: String,
    val length: Int,
    val users: Set<String>,
    val createdAt: String,
) {
    companion object {
        fun fromRoom(room: Room<PacketClient>): RoomDTO {
            val users = room.sessions.keys
            return RoomDTO(
                name = room.name,
                length = users.size,
                users = users,
                createdAt = room.createdAt.toString(),
            )
        }
    }
}
