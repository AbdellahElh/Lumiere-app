package com.example.riseandroid.ui.screens.homepage.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SlidingButtonForHomepage(
    selectedIndex: Int,
    onToggle: (Int) -> Unit,
    options: List<String>
) {
    val buttonWidth = with(LocalConfiguration.current) {
        screenWidthDp.dp / options.size
    }
    val buttonPadding = 10.dp
    val cornerRadius = 12.dp

    Row(
        modifier = Modifier
            .width(buttonWidth * options.size)
            .background(Color(0xFF32363D), RoundedCornerShape(cornerRadius))
            .clip(RoundedCornerShape(cornerRadius))
            .padding(buttonPadding)
    ) {
        options.forEachIndexed { index, text ->
            Box(
                modifier = Modifier
                    .width(buttonWidth)
                    .background(
                        color = if (selectedIndex == index) Color(0xFFE5CB77) else Color.Transparent,
                        shape = RoundedCornerShape(
                            topStart = if (index == 0) cornerRadius / 2 else 0.dp,
                            topEnd = if (index == options.lastIndex) cornerRadius / 2 else 0.dp,
                            bottomStart = if (index == 0) cornerRadius / 2 else 0.dp,
                            bottomEnd = if (index == options.lastIndex) cornerRadius / 2 else 0.dp
                        )
                    )
                    .clickable { onToggle(index) }
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = text,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (selectedIndex == index) Color.White else Color.White.copy(alpha = 0.5f)
                )
            }
        }
    }
}
