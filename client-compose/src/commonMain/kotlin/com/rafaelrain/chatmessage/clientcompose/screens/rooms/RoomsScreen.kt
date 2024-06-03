package com.rafaelrain.chatmessage.clientcompose.screens.rooms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.rafaelrain.chatmessage.clientcompose.screens.messageroom.MessageRoomScreen
import com.rafaelrain.chatmessage.sdk.ChatMessageSdk
import com.rafaelrain.chatmessage.sdk.client.Room

class RoomsScreen(private val sdk: ChatMessageSdk, private val name: String) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        val screenModel = rememberScreenModel { RoomsScreenModel(sdk.roomsClient) }
        val state by screenModel.state.collectAsState()

        var isCreateRoomDialogOpen by remember { mutableStateOf(false) }

        LaunchedEffect(Unit) {
            screenModel.fetchRooms()
        }

        ContentState(
            rooms = state.rooms,
            isCreateRoomDialogOpen = isCreateRoomDialogOpen,
            onRoomCardClick = { room ->
                navigator.push(MessageRoomScreen(sdk = sdk, name = name, roomName = room.name))
            },
            onClickNewRoom = { isCreateRoomDialogOpen = true },
            onCloseNewRoomDialog = { isCreateRoomDialogOpen = false },
            onClickRefresh = { screenModel.fetchRooms() },
            onCreateNewRoom = { roomName ->
                navigator.push(MessageRoomScreen(sdk = sdk, name = name, roomName = roomName))
            },
        )
    }

    @Composable
    fun ContentState(
        rooms: List<Room> = emptyList(),
        isCreateRoomDialogOpen: Boolean = false,
        onClickNewRoom: () -> Unit = {},
        onRoomCardClick: (Room) -> Unit = {},
        onClickRefresh: () -> Unit = {},
        onCreateNewRoom: (String) -> Unit = {},
        onCloseNewRoomDialog: () -> Unit = {},
    ) {
        Scaffold(
            topBar = { Header(onClickNewRoom) },
            floatingActionButton = {
                FloatingActionButton(onClick = onClickRefresh) {
                    Icon(Icons.Filled.Refresh, contentDescription = "Refresh")
                }
            },
        ) { innerPadding ->
            Body(
                rooms = rooms,
                onRoomCardClick = onRoomCardClick,
                innerPadding = innerPadding,
            )
        }

        CreateRoomDialog(
            isCreateRoomDialogOpen = isCreateRoomDialogOpen,
            onCreateNewRoom = onCreateNewRoom,
            onCloseNewRoomDialog = onCloseNewRoomDialog,
        )
    }

    @Composable
    fun CreateRoomDialog(
        isCreateRoomDialogOpen: Boolean = false,
        onCreateNewRoom: (String) -> Unit = {},
        onCloseNewRoomDialog: () -> Unit = {},
    ) {
        if (isCreateRoomDialogOpen) {
            var roomName by remember { mutableStateOf("") }
            AlertDialog(
                title = {
                    Text(text = "Criar sala")
                },
                text = {
                    CustomTextInput(
                        value = roomName,
                        label = "Sala",
                        onChangeValue = { value -> roomName = value },
                        onClickSend = {
                            if (roomName.isNotBlank()) {
                                onCreateNewRoom(roomName)
                            }
                        },
                    )
                },
                onDismissRequest = {
                    onCloseNewRoomDialog()
                },
                confirmButton = {
                    TextButton(
                        enabled = roomName.isNotBlank(),
                        onClick = {
                            onCreateNewRoom(roomName)
                        },
                    ) {
                        Text("Criar")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            onCloseNewRoomDialog()
                        },
                    ) {
                        Text("Cancelar")
                    }
                },
            )
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun Header(onClickNewRoom: () -> Unit) {
        TopAppBar(
            colors =
                TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
            title = { Text("Salas", style = MaterialTheme.typography.titleLarge) },
            actions = {
                IconButton(onClickNewRoom) {
                    Icon(Icons.Filled.Add, contentDescription = "Adicionar sala")
                }
            },
        )
    }

    @Composable
    private fun Body(
        rooms: List<Room> = emptyList(),
        onRoomCardClick: (Room) -> Unit,
        innerPadding: PaddingValues = PaddingValues(),
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(8.dp),
            modifier = Modifier.padding(innerPadding),
        ) {
            items(rooms) { item ->
                RoomCard(item, onRoomCardClick)
            }
        }
    }

    @Composable
    fun RoomCard(
        room: Room,
        onRoomCardClick: (Room) -> Unit,
    ) {
        Card(
            modifier = Modifier.padding(4.dp),
            colors = CardDefaults.cardColors(),
            onClick = { onRoomCardClick(room) },
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.padding(12.dp),
            ) {
                Text(
                    room.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                )

                Text(
                    "${room.length} usuários online",
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }
    }

    @Composable
    private fun CustomTextInput(
        value: String = "",
        label: String = "",
        onChangeValue: (String) -> Unit,
        onClickSend: () -> Unit,
        focusRequester: FocusRequester = FocusRequester.Default,
    ) {
        TextField(
            value = value,
            onValueChange = onChangeValue,
            label = { Text(label, style = MaterialTheme.typography.labelLarge) },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
            keyboardActions = KeyboardActions(onSend = { onClickSend() }),
            singleLine = true,
            modifier =
                Modifier.onKeyEvent {
                    if (it.key.keyCode == Key.Enter.keyCode) {
                        onClickSend()
                        true
                    } else {
                        false
                    }
                }
                    .focusRequester(focusRequester),
            shape = TextFieldDefaults.shape,
        )
    }
}
