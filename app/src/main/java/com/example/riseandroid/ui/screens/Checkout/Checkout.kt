package com.example.riseandroid.ui.screens.Checkout

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.riseandroid.R
import com.example.riseandroid.data.lumiere.TicketRepository
import com.example.riseandroid.model.Ticket
import com.example.riseandroid.ui.screens.homepage.ErrorScreen
import com.example.riseandroid.ui.screens.homepage.LoadingScreen
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.forEach
import kotlin.text.forEach
import kotlin.text.toIntOrNull
@Composable
fun CheckoutScreen(
    navController: NavController,
    selectedCinema: String,
    date: String,
    selectedHour: String,
    viewModel : CheckoutViewModel = viewModel(
    factory = CheckoutViewModel.Factory
    )
) {
    when (val uiState = viewModel.checkoutUiState) {
        is CheckoutUiState.Loading -> {
            LoadingScreen()
        }

        is CheckoutUiState.Error -> {
            ErrorScreen()
        }

        is CheckoutUiState.Success -> {
            val tickets = uiState.ticketTypes
            val ticketTypes by tickets.collectAsState(initial = emptyList())

            CheckoutDetailScreen(
                ticketTypes = ticketTypes, navController = navController,
                selectedCinema = selectedCinema,
                date = date,
                selectedHour = selectedHour
            )
        }
    }
}
@Composable
fun CheckoutDetailScreen(
    navController: NavController,
    ticketTypes: List<Ticket>,
    selectedCinema: String,
    date: String,
    selectedHour: String
) {
    println(ticketTypes)

    val selectedTicketQuantities = remember { mutableStateMapOf<Ticket, Int>() }
    var isBuyingTickets by remember { mutableStateOf(true) }
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        color = MaterialTheme.colorScheme.background
    ) {
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier.verticalScroll(scrollState),
            verticalArrangement = Arrangement.Top
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.btn_back),
                    contentDescription = "Back",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { navController.popBackStack() }
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "Tickets",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "",

                )
            }
            Spacer(modifier = Modifier.size(32.dp))
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            SlidingButton(
                isSelected = isBuyingTickets,
                onToggle = { isBuyingTickets = !isBuyingTickets },
                leftText = "Koop Tickets",
                rightText = "Gebruik Pas"
            )
                }
            Spacer(modifier = Modifier.size(32.dp))
            if (isBuyingTickets) {

                Text(
                    text = "Selecteer Tickets",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.size(18.dp))

                ticketTypes.forEach { ticketType ->
                    val quantity = selectedTicketQuantities.getOrElse(ticketType) { 0 }

                    TicketQuantityInput(
                        ticketType = ticketType,
                        quantity = quantity,
                        onQuantityChange = { newQuantity ->
                            selectedTicketQuantities[ticketType] = newQuantity
                        }
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

                val totalPrice = selectedTicketQuantities.entries.sumOf { (ticketType, quantity) ->
                    ticketType.price * quantity
                }

                Text(
                    text = "Total Price: €$totalPrice",
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            } else {
                Text(
                    text = "Selecteer Pas",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        }
    }
}
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
@Composable
fun SlidingButton(
    isSelected: Boolean,
    onToggle: () -> Unit,
    leftText: String,
    rightText: String
) {
    val buttonWidth = with(LocalConfiguration.current) {
        screenWidthDp.dp * 0.4f
    }
    val buttonPadding = 10.dp
    val cornerRadius = 12.dp

    Row(
        modifier = Modifier
            .width(buttonWidth * 2)
            .background(Color(0xFF32363D), RoundedCornerShape(cornerRadius))
            .clip(RoundedCornerShape(cornerRadius))
            .padding(buttonPadding),

    ) {
        Box(
            modifier = Modifier
                .width(buttonWidth)
                .background(
                    color = if (isSelected) Color(0xFFE5CB77) else Color.Transparent,
                    shape = RoundedCornerShape(
                        topStart = if (isSelected) cornerRadius/2 else 0.dp,
                        topEnd = if (isSelected) cornerRadius/2 else 0.dp,
                        bottomStart = if (isSelected) cornerRadius/2 else 0.dp,
                        bottomEnd = if (isSelected) cornerRadius/2 else 0.dp
                    )
                )
                .clickable { onToggle() }
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = leftText,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = if (isSelected) Color.White else Color.White.copy(alpha = 0.5f)
            )
        }

        Box(
            modifier = Modifier
                .width(buttonWidth)
                .background(
                    color = if (!isSelected) Color(0xFFE5CB77) else Color.Transparent,
                    shape = RoundedCornerShape(
                        topEnd = if (!isSelected) cornerRadius/2 else 0.dp,
                        topStart = if (!isSelected) cornerRadius/2 else 0.dp,
                        bottomEnd = if (!isSelected) cornerRadius/2 else 0.dp,
                        bottomStart = if (!isSelected) cornerRadius/2 else 0.dp
                    )
                )
                .clickable { onToggle() }
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = rightText,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = if (!isSelected) Color.White else Color.White.copy(alpha = 0.5f)
            )
        }
    }
}