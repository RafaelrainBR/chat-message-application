package com.rafaelrain.chatmessage.clientcompose.screens.messageroom

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import com.rafaelrain.chatmessage.sdk.client.MessageSessionClient

data class Message(
    val senderName: String,
    val message: String,
    val isFromUser: Boolean = false,
)

val client: MessageSessionClient by lazy {
    ChatMessageSdk.create().messageSessionClient
}

class MessageRoomScreen(
    private val name: String,
    private val roomName: String,
) : Screen {
    @Composable
    override fun Content() {
        val screenModel =
            rememberScreenModel {
                MessageRoomScreenModel(
                    name = name,
                    roomName = roomName,
                    messageSessionClient = client,
                )
            }
        val state by screenModel.state.collectAsState()

        ContentState(
            roomName = state.roomName,
            isConnected = state.isConnected,
            messages = state.messages,
            message = state.message,
            onClickConnect = { screenModel.connect() },
            onChangeMessage = { message -> screenModel.setMessage(message) },
            onClickSend = { screenModel.handleSend() },
        )
    }

    @Composable
    fun ContentState(
        roomName: String = "",
        isConnected: Boolean = false,
        messages: List<Message> = emptyList(),
        message: String = "",
        onClickConnect: () -> Unit = {},
        onChangeMessage: (String) -> Unit = {},
        onClickSend: () -> Unit = {},
    ) {
        val lazyListState = rememberLazyListState(0)
        LaunchedEffect(messages) {
            lazyListState.animateScrollToItem(messages.size)
        }

        Scaffold(
            topBar = {
                Header(
                    roomName = roomName,
                    isConnected = isConnected,
                    onClickConnect = onClickConnect,
                )
            },
            bottomBar = {
                Bottom(
                    isConnected = isConnected,
                    message = message,
                    onChangeMessage = onChangeMessage,
                    onClickSend = onClickSend,
                )
            },
        ) { innerPadding ->
            Body(innerPadding = innerPadding, messages = messages, lazyListState = lazyListState)
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun Header(
        roomName: String = "",
        isConnected: Boolean = false,
        onClickConnect: () -> Unit = {},
    ) {
        TopAppBar(
            colors =
                TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
            title = { Text(roomName, style = MaterialTheme.typography.titleLarge) },
            actions = {
                TextButton(
                    onClick = onClickConnect,
                    enabled = !isConnected,
                ) {
                    Text("Conectar", style = MaterialTheme.typography.titleSmall)
                }
            },
        )
    }

    @Composable
    private fun Bottom(
        isConnected: Boolean = false,
        message: String = "",
        onChangeMessage: (String) -> Unit = {},
        onClickSend: () -> Unit = {},
    ) {
        BottomAppBar(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.primary,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier =
                    Modifier
                        .padding(horizontal = 8.dp)
                        .fillMaxSize(),
            ) {
                TextField(
                    value = message,
                    onValueChange = onChangeMessage,
                    enabled = isConnected,
                    label = { Text("Mensagem", style = MaterialTheme.typography.labelLarge) },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                    keyboardActions =
                        KeyboardActions(
                            onSend = {
                                onClickSend()
                            },
                        ),
                    singleLine = true,
                    modifier =
                        Modifier.onKeyEvent {
                            if (it.key.keyCode == Key.Enter.keyCode) {
                                onClickSend()
                                true
                            } else {
                                false
                            }
                        },
                    shape = TextFieldDefaults.shape,
                )

                FilledIconButton(
                    onClick = onClickSend,
                    modifier = Modifier.fillMaxSize(0.7F),
                    enabled = isConnected,
                ) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.Send, contentDescription = "Send")
                }
            }
        }
    }

    @Composable
    private fun Body(
        innerPadding: PaddingValues = PaddingValues(),
        messages: List<Message> = emptyList(),
        lazyListState: LazyListState = LazyListState(0),
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier =
                Modifier
                    .padding(innerPadding)
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .fillMaxSize(),
            state = lazyListState,
        ) {
            items(messages) {
                MessageCard(it)
            }
        }
    }

    @Composable
    private fun MessageCard(message: Message) {
        Row(
            horizontalArrangement = if (message.isFromUser) Arrangement.End else Arrangement.Start,
            modifier = Modifier.fillMaxWidth(),
        ) {
            ElevatedCard(
                onClick = {},
                elevation = CardDefaults.cardElevation(6.dp),
                modifier =
                    Modifier
                        .padding(horizontal = 24.dp),
            ) {
                Column(
                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp).fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceAround,
                ) {
                    Text(message.senderName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(message.message, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}
