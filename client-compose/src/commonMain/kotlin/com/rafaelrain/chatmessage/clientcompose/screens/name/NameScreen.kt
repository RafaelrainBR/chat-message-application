package com.rafaelrain.chatmessage.clientcompose.screens.name

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.rafaelrain.chatmessage.clientcompose.screens.rooms.RoomsScreen
import com.rafaelrain.chatmessage.sdk.ChatMessageSdk

class NameScreen(
    private val sdk: ChatMessageSdk,
) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        var name by remember { mutableStateOf("") }

        val isSendButtonEnabled = name.isNotBlank()

        ContentState(
            name = name,
            isSendButtonEnabled = isSendButtonEnabled,
            onChangeName = { name = it },
            onClickSend = {
                navigator.push(RoomsScreen(sdk = sdk, name = name))
            },
        )
    }

    @Composable
    fun ContentState(
        name: String = "",
        isSendButtonEnabled: Boolean = false,
        onChangeName: (String) -> Unit = {},
        onClickSend: () -> Unit = {},
    ) {
        Scaffold(
            topBar = { Header() },
        ) { innerPadding ->
            Body(
                name = name,
                isSendButtonEnabled = isSendButtonEnabled,
                onChangeName = onChangeName,
                onClickSend = onClickSend,
                innerPadding = innerPadding,
            )
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun Header() {
        TopAppBar(
            colors =
                TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
            title = { Text("Mensagens", style = MaterialTheme.typography.titleLarge) },
        )
    }

    @Composable
    private fun Body(
        name: String = "",
        isSendButtonEnabled: Boolean = false,
        onChangeName: (String) -> Unit = {},
        onClickSend: () -> Unit = {},
        innerPadding: PaddingValues = PaddingValues(),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier =
                Modifier
                    .padding(innerPadding)
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .fillMaxSize(),
        ) {
            CustomTextInput(
                label = "Nome",
                value = name,
                onChangeValue = onChangeName,
                onClickSend = {
                    if (isSendButtonEnabled) {
                        onClickSend()
                    }
                },
            )

            ElevatedButton(onClick = onClickSend, enabled = isSendButtonEnabled) {
                Text("Entrar")
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
