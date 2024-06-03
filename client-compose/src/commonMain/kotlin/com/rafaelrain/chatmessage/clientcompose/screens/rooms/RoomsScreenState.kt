package com.rafaelrain.chatmessage.clientcompose.screens.rooms

import com.rafaelrain.chatmessage.sdk.client.Room

data class RoomsScreenState(
    val rooms: List<Room> = emptyList(),
)
