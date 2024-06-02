package com.rafaelrain.chatmessage.sdk.session

import com.rafaelrain.chatmessage.common.packet.ClientMessageSessionPacket
import com.rafaelrain.chatmessage.common.packet.ServerMessageSessionPacket
import kotlinx.coroutines.flow.MutableSharedFlow

class MessageSession(
    val name: String,
    val roomName: String,
) {
    private val incomingPacketsFlow = MutableSharedFlow<ServerMessageSessionPacket>()
    private val outgoingPacketsFlow = MutableSharedFlow<ClientMessageSessionPacket>()

    suspend fun listenClientPackets(collect: suspend (ClientMessageSessionPacket) -> Unit): Nothing {
        outgoingPacketsFlow.collect(collect)
    }

    suspend fun listenServerPackets(collect: suspend (ServerMessageSessionPacket) -> Unit): Nothing {
        incomingPacketsFlow.collect(collect)
    }

    suspend fun emitClientPacket(packet: ClientMessageSessionPacket) {
        outgoingPacketsFlow.emit(packet)
    }

    suspend fun emitServerPacket(packet: ServerMessageSessionPacket) {
        incomingPacketsFlow.emit(packet)
    }
}
