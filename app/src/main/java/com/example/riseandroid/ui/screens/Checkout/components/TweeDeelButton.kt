package com.example.riseandroid.ui.screens.Checkout.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TweeDeelButton(price: Double) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFE5CB77)),
    ) {
        Box(
            modifier = Modifier
                .weight(0.6f)
                .fillMaxHeight()
                .background(Color(0xFFE5CB77))
                .padding(12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Betaal nu",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
        }
        Divider(
            color = Color.White,
            modifier = Modifier
                .fillMaxHeight()
                .width(1.5.dp)
                .padding(vertical = 13.dp)

        )
        Box(
            modifier = Modifier
                .weight(0.4f)
                .fillMaxHeight()
                .background(Color(0xFFE5CB77)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "€${price}",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}