package com.example.riseandroid.ui.screens.Checkout.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.riseandroid.R
import com.example.riseandroid.model.CardInfo

@Composable
fun PaymentCardInput(
    onCardAdded: (CardInfo) -> Unit,
    initialCardInfo: CardInfo? = null
) {
    var showDialog by remember { mutableStateOf(false) }
    var cardInfo by remember { mutableStateOf(initialCardInfo ?: CardInfo("", "", "", "")) }
    Column (horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Betalingsmethode",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
            Text(
                text = if (cardInfo.cardNumber == "") {
                    ""
                } else {
                    "Edit"
                },
                modifier = Modifier.clickable { showDialog = true },
                fontSize = 14.sp,
                fontWeight = FontWeight.Light,
                color = Color(0xFFB2B5BB)
            )
        }
        Spacer(modifier = Modifier.size(20.dp))
        if (cardInfo.cardNumber == "") {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .clip(RoundedCornerShape(16.dp))
                    .drawBehind {
                        drawRoundRect(
                            color = Color.White.copy(alpha = 0.8f),
                            style = Stroke(
                                width = 1.dp.toPx(),
                                pathEffect = PathEffect.dashPathEffect(
                                    floatArrayOf(10f, 10f)
                                )
                            ),
                            cornerRadius = CornerRadius(16.dp.toPx())
                        )
                    }
                    .clickable { showDialog = true }
                    .aspectRatio(2f),
                contentAlignment = Alignment.Center
            ) {

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Add, contentDescription = "Add Card", tint = Color.White)
                    Spacer(Modifier.width(8.dp))
                    Text("Card", color = Color.White, fontSize = 14.sp)
                }
            }
        }
        else {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { showDialog = true }

                    .aspectRatio(2f),


                contentAlignment = Alignment.BottomStart
            ) {
                Image(
                    painter = painterResource(id = R.drawable.card_layout),
                    contentDescription = "Kaart achtergrond",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.matchParentSize()
                )

                Box(
                    modifier = Modifier
                        .background(Color(0xFF007CD7))
                        .padding(16.dp)

                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(

                        ) {
                            Text("Kaart houder", color = Color.White.copy(0.8F), fontSize = 12.sp)

                            Text(cardInfo.name, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, lineHeight = 0.7.sp)
                        }
                        Text("**** **** **** ${cardInfo.last4Digits}", color = Color.White, fontSize = 12.sp)
                    }
                }


            }
        }
    }
    if (showDialog) {
        PaymentCardDialog(
            onDismiss = { showDialog = false },
            onCardAdded = { info ->
                showDialog = false
                cardInfo = info
                onCardAdded(info)
            },
            initialCardInfo = cardInfo
        )
    }
}