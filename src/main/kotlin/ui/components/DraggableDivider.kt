package ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import ui.theme.DividerColor
import ui.theme.DividerHighlightColor
import java.awt.Cursor

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DraggableDivider(
    splitPosition: Float,
    onPositionChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    var isHoveringOnDivider by remember { mutableStateOf(false) }
    var isDraggingDivider by remember { mutableStateOf(false) }

    val dividerColor by animateColorAsState(
        targetValue = if (isHoveringOnDivider || isDraggingDivider)
            DividerHighlightColor else DividerColor
    )

    val dividerWidth by animateDpAsState(
        targetValue = if (isHoveringOnDivider || isDraggingDivider)
            8.dp else 2.dp
    )

    Box(
        modifier = modifier
            .fillMaxHeight()
            .width(dividerWidth)
            .background(dividerColor)
            .pointerHoverIcon(PointerIcon(Cursor(Cursor.E_RESIZE_CURSOR)))
            .onPointerEvent(PointerEventType.Enter) {
                isHoveringOnDivider = true
            }
            .onPointerEvent(PointerEventType.Exit) {
                isHoveringOnDivider = false
            }
            .draggable(
                orientation = Orientation.Horizontal,
                state = rememberDraggableState { delta ->
                    val newPosition = splitPosition + delta / 2000f // 2000 for smooth drag
                    onPositionChange(newPosition.coerceIn(0.2f, 0.5f))
                },
                onDragStarted = {
                    isDraggingDivider = true
                },
                onDragStopped = {
                    isDraggingDivider = false
                }
            )
    )
}