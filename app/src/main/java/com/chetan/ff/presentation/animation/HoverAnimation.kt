package com.chetan.ff.presentation.animation

import android.view.MotionEvent
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.chetan.ff.presentation.dashboard.MenuItem
import kotlinx.coroutines.delay

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun HoverAnimation(menuItems: List<MenuItem>,onClick: (MenuItem) -> Unit,) {

    // Indicates if the icons are visible or not (Used for animation)
    var visible by remember {
        mutableStateOf(false)
    }

    // Indicates the hovererd item index
    var hoverIndex by remember {
        mutableIntStateOf(-1)
    }

    // Indicates the selected Index

    var selectedIndex by remember {
        mutableIntStateOf(-1)
    }

    val density = LocalDensity.current
    val hoverScale = 1.5f
    val iconSize = 48.dp
    val columnContentPadding = 8.dp
    val iconSizePx = with(density) {iconSize.toPx()}
    val boxPaddingPx = with(density) {columnContentPadding.toPx()}
    val iconHoveredSize = iconSize.times(hoverScale)

    val boxHeight = iconHoveredSize +
            columnContentPadding.times(2)
    val boxHeightPx = with(density) {boxHeight.toPx()}
    
    Box(modifier = Modifier
        .pointerInteropFilter {
            val selection = ((it.y - boxPaddingPx) / iconSizePx).toInt()
            if (selection >= menuItems.size || selection <0 || it.y < boxPaddingPx || it.x > boxHeightPx){
                hoverIndex = -1
            }else if (it.action == MotionEvent.ACTION_UP && it.x < boxHeightPx){
                selectedIndex = hoverIndex
//                onClick(menuItems[hoverIndex])

                println("clicked")
                hoverIndex = -1
            }else{
                hoverIndex = selection
            }
            true
        }

    ){
        // Row containing the items
        Column(
            Modifier
                .align(Alignment.BottomStart)
                .width(IntrinsicSize.Min)
                .padding(columnContentPadding),

        ) {
            menuItems.forEachIndexed { index, item ->
                val size = if (hoverIndex == index) iconHoveredSize else iconSize

                    ReactionItem(
                        text = item.label,
                        icon = item.icon,
                        size = animateDpAsState(size).value,
                        isHovered = hoverIndex == index,
                        isSelected = index == selectedIndex,
                    )

            }
        }
        // Play the start animation after the first composition
        LaunchedEffect(Unit) {
            delay(500)
            visible = true
        }
    }

}
@Composable
fun ReactionItem(
    text: String,
    icon: ImageVector,
    size: Dp,
    isHovered: Boolean,
    isSelected: Boolean,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            Modifier
                .size(size)
                .padding(vertical = 2
                    .dp)
                .border(1.dp, Color.LightGray, CircleShape)
                .background(Color.White, CircleShape)
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = if (isSelected) MaterialTheme.colorScheme.primary else Color.DarkGray,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            )
        }
        if (isHovered) {
            Text(
                text = text,
                color = Color.White,
                modifier = Modifier
                    .background(
                        Color.Black,
                        RoundedCornerShape(4.dp)
                    )
                    .padding(4.dp)
            )
        }
    }
}