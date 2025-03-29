package ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.sharp.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.key.*
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import beautifyJson
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import loadFile
import ui.components.DraggableDivider
import ui.components.JsonTreeView
import ui.components.Tooltip
import ui.theme.EditorBackgroundColor
import ui.theme.HeaderBackgroundColor
import ui.theme.ViewerBackgroundColor
import java.awt.Toolkit
import java.awt.datatransfer.DataFlavor

@Composable
fun MainScreen() {
    var splitPosition by remember { mutableStateOf(0.4f) }
    var jsonText by remember { mutableStateOf("") }
    var jsonElement by remember { mutableStateOf<JsonElement?>(null) }
    val clipboardManager = LocalClipboardManager.current

    jsonElement = try {
        if (jsonText.isNotBlank()) {
            Json.parseToJsonElement(jsonText)
        } else {
            null
        }
    } catch (e: Exception) {
        null
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .background(color = HeaderBackgroundColor)
                .padding(horizontal = 8.dp)
        ) {

            Tooltip(tooltip = "Open JSON File") {
                IconButton(modifier = Modifier.align(Alignment.CenterStart), onClick = {
                    val fileContent = loadFile()
                    if (fileContent.isNotBlank()) {
                        jsonText = fileContent
                    }
                }) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Open file",
                        tint = Color.White
                    )
                }
            }


            Row(modifier = Modifier.align(Alignment.CenterEnd)) {
                // Clear button
                Tooltip(tooltip = "Clear Editor") {
                    IconButton(
                        onClick = {
                            jsonText = ""
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear JSON",
                            tint = Color.White
                        )
                    }
                }

                // Format JSON button
                Tooltip(tooltip = "Format JSON (Ctrl+L)") {
                    IconButton(
                        onClick = { jsonText = jsonText.beautifyJson() }
                    ) {
                        Icon(
                            imageVector = Icons.Sharp.Refresh,
                            contentDescription = "Format JSON",
                            tint = Color.White
                        )
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(color = Color(0xff353535))
        )

        Row(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .weight(splitPosition)
                    .fillMaxHeight()
                    .background(EditorBackgroundColor)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                TextField(
                    value = jsonText,
                    onValueChange = { jsonText = it },
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = Color.White,
                        backgroundColor = EditorBackgroundColor,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = Color.White
                    ),
                    shape = RectangleShape,
                    modifier = Modifier
                        .fillMaxSize()
                        .onKeyEvent { event ->
                            when (event.type) {
                                KeyEventType.KeyDown -> {
                                    // Format JSON with Ctrl+L
                                    if (event.isCtrlPressed && event.key == Key.L) {
                                        jsonText = jsonText.beautifyJson()
                                        true
                                    }
                                    // Paste with Ctrl+V
                                    else if (event.isCtrlPressed && event.key == Key.V) {
                                        try {
                                            val clipboard = Toolkit.getDefaultToolkit().systemClipboard
                                            val clipboardText = clipboard.getData(DataFlavor.stringFlavor) as String
                                            clipboardManager.getText()?.let {
                                                jsonText = clipboardText
                                            }
                                        } catch (e: Exception) {
                                            e.printStackTrace()
                                        }
                                        true
                                    } else {
                                        false
                                    }
                                }

                                else -> false
                            }
                        }
                )
            }

            DraggableDivider(
                splitPosition = splitPosition,
                onPositionChange = { splitPosition = it }
            )

            Box(
                modifier = Modifier
                    .weight(1f - splitPosition)
                    .fillMaxHeight()
                    .background(ViewerBackgroundColor)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                if (jsonElement != null) {
                    JsonTreeView(
                        jsonElement = jsonElement,
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .horizontalScroll(rememberScrollState())
                    )
                } else if (jsonText.isNotBlank()) {
                    Text(
                        "Invalid JSON",
                        color = Color.Red,
                        fontSize = 30.sp,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}