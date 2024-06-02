package com.rafaelrain.chatmessage.clientcompose

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import com.rafaelrain.chatmessage.clientcompose.screens.messageroom.MessageRoomScreen
import com.rafaelrain.chatmessage.clientcompose.ui.theme.AppTheme

@Composable
fun App() =
    AppTheme {
        Navigator(MessageRoomScreen())
    }
