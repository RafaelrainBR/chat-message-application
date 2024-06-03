package com.rafaelrain.chatmessage.clientcompose

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import com.rafaelrain.chatmessage.clientcompose.screens.name.NameScreen
import com.rafaelrain.chatmessage.clientcompose.ui.theme.AppTheme
import com.rafaelrain.chatmessage.sdk.ChatMessageSdk

@Composable
fun App() =
    AppTheme {
        val sdk = ChatMessageSdk.create(host = "0.tcp.sa.ngrok.io", port = 15345)

        Navigator(NameScreen(sdk))
    }
