package ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import isUrl
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.floatOrNull
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.longOrNull
import ui.theme.*


@Composable
fun JsonTreeView(jsonElement: JsonElement?, modifier: Modifier = Modifier) {
    if (jsonElement == null) return

    Column(modifier = modifier.fillMaxSize()) {
        when (jsonElement) {
            is JsonObject -> JsonObjectView(jsonElement)
            is JsonArray -> JsonArrayView(jsonElement)
            is JsonPrimitive -> JsonPrimitiveView(jsonElement)
            JsonNull -> Text("null", color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f))
        }
    }
}

@Composable
fun JsonObjectView(jsonObject: JsonObject, indent: Int = 0) {
    var expanded by remember { mutableStateOf(true) }
    val indentPadding = (indent * 16).dp

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded }
            .padding(start = indentPadding)
    ) {
        Icon(
            imageVector = if (expanded) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowRight,
            tint = JsonKeyColor,
            contentDescription = if (expanded) "Collapse" else "Expand"
        )
        Text("{} ${jsonObject.size} keys", color = JsonArrayObjectLabelColor)
    }

    AnimatedVisibility(expanded && jsonObject.isNotEmpty()) {
        Column(modifier = Modifier.padding(start = indentPadding + 16.dp)) {
            jsonObject.entries.forEach { (key, value) ->
                Row(verticalAlignment = Alignment.Top) {
                    Text(
                        text = "\"${key}\": ",
                        fontWeight = FontWeight.Bold,
                        color = JsonKeyColor
                    )
                    JsonTreeView(jsonElement = value)
                }
            }
        }
    }
}


@Composable
fun JsonArrayView(jsonArray: JsonArray, indent: Int = 0) {
    var expanded by remember { mutableStateOf(true) }
    val indentPadding = (indent * 16).dp

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded }
            .padding(start = indentPadding)
    ) {
        Icon(
            imageVector = if (expanded) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowRight,
            tint = JsonKeyColor,
            contentDescription = if (expanded) "Collapse" else "Expand"
        )
        Text("[] ${jsonArray.size} items", color = JsonArrayObjectLabelColor)
    }

    AnimatedVisibility(expanded && jsonArray.isNotEmpty()) {
        Column(modifier = Modifier.padding(start = indentPadding + 16.dp)) {
            jsonArray.forEachIndexed { index, element ->
                Row {
                    Text(
                        text = "$index: ",
                        fontWeight = FontWeight.Bold,
                        color = JsonKeyColor
                    )
                    JsonTreeView(jsonElement = element)
                }
            }
        }
    }
}

@Composable
fun JsonPrimitiveView(jsonPrimitive: JsonPrimitive) {
    val uriHandler = LocalUriHandler.current
    val isUrl = jsonPrimitive.isUrl()

    val text = when {
        isUrl -> jsonPrimitive.content
        jsonPrimitive.isString -> "\"${jsonPrimitive.content}\""
        else -> jsonPrimitive.content
    }

    val color = when {
        isUrl -> JsonUrlColor
        jsonPrimitive.isString -> JsonStringColor
        jsonPrimitive.booleanOrNull != null -> JsonBooleanColor
        jsonPrimitive.intOrNull != null || jsonPrimitive.longOrNull != null ||
                jsonPrimitive.doubleOrNull != null || jsonPrimitive.floatOrNull != null -> JsonNumberColor
        else -> MaterialTheme.colors.onSurface
    }

    val textDecoration = if (isUrl) TextDecoration.Underline else TextDecoration.None

    val modifier = if (isUrl) {
        Modifier.clickable { uriHandler.openUri(jsonPrimitive.content) }
    } else {
        Modifier
    }

    Text(
        text = text,
        color = color,
        textDecoration = textDecoration,
        modifier = modifier
    )
}