import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import ui.screens.MainScreen
import ui.theme.JsonVisualizerTheme


fun main() = application {
    val windowState = rememberWindowState(
        size = DpSize(1024.dp, 768.dp)
    )

    Window(
        onCloseRequest = ::exitApplication,
        title = "JSON Visualizer",
        state = windowState,
        resizable = true,
        icon = painterResource(resourcePath = "icons/app-logo.svg")
    ) {
        window.minimumSize = java.awt.Dimension(800, 800)

         JsonVisualizerTheme {
            MainScreen()
        }
    }
}