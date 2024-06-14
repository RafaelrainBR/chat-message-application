package com.rafaelrain.chatmessage.clientcompose

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import com.rafaelrain.chatmessage.clientcompose.screens.name.NameScreen
import com.rafaelrain.chatmessage.clientcompose.ui.theme.AppTheme
import com.rafaelrain.chatmessage.sdk.ChatMessageSdk

@Composable
fun App() =
    AppTheme {
        val sdk =
            ChatMessageSdk.create(
                apiUrl = "https://application-ktor.fly.dev",
                webSocketHost = "application-ktor.fly.dev",
                webSocketPort = 80,
            )

        Navigator(NameScreen(sdk))
    }
