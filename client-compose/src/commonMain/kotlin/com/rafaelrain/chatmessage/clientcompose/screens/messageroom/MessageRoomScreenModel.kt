package com.rafaelrain.chatmessage.clientcompose.screens.messageroom

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.rafaelrain.chatmessage.common.packet.ClientMessageSessionPacket
import com.rafaelrain.chatmessage.common.packet.ClientMessageSessionPacketType
import com.rafaelrain.chatmessage.common.packet.ServerMessageSessionPacketType
import com.rafaelrain.chatmessage.sdk.client.CreateSessionRequest
import com.rafaelrain.chatmessage.sdk.client.MessageSessionClient
import kotlinx.coroutines.launch

class MessageRoomScreenModel(
    private val messageSessionClient: MessageSessionClient,
    private val name: String,
    private val roomName: String,
) : StateScreenModel<MessageRoomScreenState>(MessageRoomScreenState(name = name, roomName = roomName)) {
    fun connect() =
        screenModelScope.launch {
            val session =
                messageSessionClient.createSession(
                    createSessionRequest = CreateSessionRequest(name = name, roomName = roomName),
                    coroutineScope = screenModelScope,
                )

            mutableState.emit(state.value.copy(messageSession = session, isConnected = true))
            listenPackets()
        }

    fun handleSend() =
        screenModelScope.launch {
            state.value.messageSession?.emitClientPacket(
                ClientMessageSessionPacket(
                    type = ClientMessageSessionPacketType.MESSAGE,
                    roomName = roomName,
                    senderName = name,
                    message = state.value.message,
                ),
            )
            mutableState.emit(state.value.copy(message = ""))
        }

    fun setMessage(message: String) {
        mutableState.tryEmit(state.value.copy(message = message))
    }

    private fun listenPackets() =
        screenModelScope.launch {
            state.value.messageSession?.listenServerPackets {
                if (it.type == ServerMessageSessionPacketType.MESSAGE) {
                    val message =
                        Message(
                            senderName = it.senderName,
                            message = it.message.orEmpty(),
                            isFromUser = it.senderName == name,
                        )
                    mutableState.emit(state.value.copy(messages = (state.value.messages + message)))
                }
            }
        }
}
