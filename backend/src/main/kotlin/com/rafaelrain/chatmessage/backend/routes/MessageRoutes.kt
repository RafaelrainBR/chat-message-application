package com.rafaelrain.chatmessage.backend.routes

import com.rafaelrain.chatmessage.common.packet.ClientMessageSessionPacket
import com.rafaelrain.chatmessage.common.packet.ServerMessageSessionPacket
import com.rafaelrain.chatmessage.common.packet.ServerMessageSessionPacketType
import io.ktor.server.application.call
import io.ktor.server.request.header
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.websocket.WebSocketServerSession
import io.ktor.server.websocket.sendSerialized
import io.ktor.server.websocket.webSocket
import io.ktor.websocket.CloseReason
import io.ktor.websocket.CloseReason.Codes.CANNOT_ACCEPT
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.time.LocalDateTime
import java.util.Collections

data class MessageHistory(
    val sender: String,
    val message: String,
    val sentAt: LocalDateTime,
)

data class Room(
    val name: String,
    val sessions: MutableList<MessageSession> = mutableListOf(),
    val messages: MutableList<MessageHistory> = mutableListOf(),
) {
    suspend fun broadcast(packet: ServerMessageSessionPacket) {
        sessions.forEach { session ->
            session.socketSession.sendSerialized(packet)
        }
    }

    fun removeSession(session: MessageSession) {
        sessions.removeIf { it.name == session.name }
    }

    fun saveMessage(
        session: MessageSession,
        message: String,
    ) {
        messages.add(
            MessageHistory(
                sender = session.name,
                message = message,
                sentAt = LocalDateTime.now(),
            ),
        )
    }
}

data class MessageSession(
    val socketSession: WebSocketServerSession,
    val name: String,
    val roomName: String,
)

object Rooms {
    private val storage = Collections.synchronizedMap<String, Room>(HashMap())

    fun getRooms(): List<RoomDTO> = storage.values.map { RoomDTO.fromRoom(it) }

    suspend fun addNewSession(session: MessageSession) {
        val room = getRoomOrCreate(session.roomName)

        room.removeSession(session)
        room.sessions.add(session)
        notifyJoin(room, session)

        val sortedMessages = room.messages.sortedBy { it.sentAt }

        sortedMessages.forEach { message ->
            val packet =
                ServerMessageSessionPacket(
                    type = ServerMessageSessionPacketType.MESSAGE,
                    roomName = room.name,
                    senderName = message.sender,
                    message = message.message,
                )
            session.socketSession.sendSerialized(packet)
        }
    }

    suspend fun notifyMessage(
        session: MessageSession,
        message: String,
    ) {
        val room = getRoomOrCreate(session.roomName)
        val packet =
            ServerMessageSessionPacket(ServerMessageSessionPacketType.MESSAGE, room.name, session.name, message)
        room.broadcast(packet)
        room.saveMessage(session, message)
    }

    suspend fun handleLeave(session: MessageSession) {
        val room = getRoomOrCreate(session.roomName)
        room.removeSession(session)

        val packet = ServerMessageSessionPacket(ServerMessageSessionPacketType.LEAVE, room.name, session.name)
        room.broadcast(packet)
    }

    private suspend fun notifyJoin(
        room: Room,
        session: MessageSession,
    ) {
        val packet = ServerMessageSessionPacket(ServerMessageSessionPacketType.JOINED, room.name, session.name)
        room.broadcast(packet)
    }

    private fun getRoomOrCreate(name: String): Room {
        val existingRoom = storage[name]
        return if (existingRoom != null) {
            existingRoom
        } else {
            val newRoom = Room(name, mutableListOf())
            storage[name] = newRoom
            newRoom
        }
    }
}

fun Route.messageRoutes() {
    get("/rooms") {
        call.respond(Rooms.getRooms())
    }

    webSocket("/messages") {
        val userName =
            call.request.header("Name")
                ?: return@webSocket close(CloseReason(CANNOT_ACCEPT, "Name header not found"))

        val roomName =
            call.request.header("Room")
                ?: return@webSocket close(CloseReason(CANNOT_ACCEPT, "Room header not found"))

        val session =
            MessageSession(
                socketSession = this,
                name = userName,
                roomName = roomName,
            )
        Rooms.addNewSession(session)

        runCatching {
            for (frame in incoming) {
                if (frame is Frame.Text) {
                    runCatching {
                        val text = frame.readText()
                        val packet = Json.decodeFromString<ClientMessageSessionPacket>(text)
                        val message = packet.message
                        if (!message.isNullOrBlank()) {
                            Rooms.notifyMessage(session, message)
                        }
                    }.onFailure { it.printStackTrace() }
                } else if (frame is Frame.Close) {
                    Rooms.handleLeave(session)
                }
            }
        }.getOrElse { error ->
            if (error is ClosedReceiveChannelException) {
                Rooms.handleLeave(session)
            } else {
                throw error
            }
        }

        Rooms.handleLeave(session)
    }
}

@Serializable
data class RoomDTO(
    val name: String,
    val length: Int,
    val users: List<String>,
) {
    companion object {
        fun fromRoom(room: Room): RoomDTO {
            val users = room.sessions.map { it.name }
            return RoomDTO(
                name = room.name,
                length = users.size,
                users = users,
            )
        }
    }
}
