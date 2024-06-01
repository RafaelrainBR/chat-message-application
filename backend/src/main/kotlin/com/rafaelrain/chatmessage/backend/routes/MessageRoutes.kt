package com.rafaelrain.chatmessage.backend.routes

import com.rafaelrain.chatmessage.backend.routes.PacketType.JOINED
import com.rafaelrain.chatmessage.backend.routes.PacketType.LEAVE
import com.rafaelrain.chatmessage.backend.routes.PacketType.MESSAGE
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
import java.util.Collections

data class Room(
    val name: String,
    val sessions: MutableList<MessageSession>,
) {
    suspend fun broadcast(packet: SocketPacket) {
        sessions.forEach { session ->
            session.socketSession.sendSerialized(packet)
        }
    }

    suspend fun removeSession(session: MessageSession) {
        sessions.removeIf { it.name == session.name }
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
    }

    suspend fun notifyMessage(
        session: MessageSession,
        message: String,
    ) {
        val room = getRoomOrCreate(session.roomName)
        val packet = SocketPacket(MESSAGE, room.name, session.name, message)
        room.broadcast(packet)
    }

    suspend fun handleLeave(session: MessageSession) {
        val room = getRoomOrCreate(session.roomName)
        room.removeSession(session)

        val packet = SocketPacket(LEAVE, room.name, session.name)
        room.broadcast(packet)
    }

    private suspend fun notifyJoin(
        room: Room,
        session: MessageSession,
    ) {
        val packet = SocketPacket(JOINED, room.name, session.name)
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
                    val message = frame.readText()
                    Rooms.notifyMessage(session, message)
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
data class SocketPacket(
    val type: PacketType,
    val roomName: String,
    val senderName: String,
    val message: String? = null,
)

@Serializable
enum class PacketType { JOINED, LEAVE, MESSAGE }

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
