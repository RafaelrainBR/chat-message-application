package com.rafaelrain.chatmessage.clientcompose.screens.rooms

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.rafaelrain.chatmessage.sdk.client.RoomsClient
import kotlinx.coroutines.launch

class RoomsScreenModel(
    private val roomsClient: RoomsClient,
) : StateScreenModel<RoomsScreenState>(RoomsScreenState()) {
    fun fetchRooms() =
        screenModelScope.launch {
            val rooms = roomsClient.getRooms()

            mutableState.emit(state.value.copy(rooms = rooms))
        }
}
