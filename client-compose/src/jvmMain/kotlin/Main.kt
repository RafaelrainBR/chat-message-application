import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.rafaelrain.chatmessage.clientcompose.App

fun main() =
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "Chat message application",
            state = rememberWindowState(width = 300.dp, height = 300.dp),
        ) {
            App()
        }
    }
