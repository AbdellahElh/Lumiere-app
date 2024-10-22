package com.example.riseandroid.ui.screens.Checkout.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.riseandroid.model.Ticket

@Composable
fun TicketQuantityInput(
    ticketType: Ticket,
    quantity: Int,
    onQuantityChange: (Int) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(8.dp)
            .drawBehind {
                val borderSize = 1.dp.toPx()
                drawLine(
                    color = Color.Gray,
                    start = Offset(0f, size.height - borderSize),
                    end = Offset(size.width, size.height - borderSize),
                    strokeWidth = borderSize
                )
            }
    ) {
        Text(
            text = "${ticketType.name} - €${ticketType.price}",
            color = Color.White,
            modifier = Modifier
                .weight(1f)
                .padding(8.dp)
        )
        IconButton(onClick = { onQuantityChange(quantity - 1) }, enabled = quantity > 0) {
            Icon(Icons.Filled.Add, contentDescription = "Decrease", tint = Color.White)
        }
        Text(text = "$quantity", modifier = Modifier.padding(horizontal = 8.dp) , color = Color.White)
        IconButton(onClick = { onQuantityChange(quantity + 1) }) {
            Icon(Icons.Filled.Add, contentDescription = "Increase" , tint = Color.White)
        }
    }
}