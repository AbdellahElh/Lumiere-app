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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.height
import androidx.compose.ui.unit.size
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.riseandroid.R
import com.example.riseandroid.data.lumiere.TicketRepository
import com.example.riseandroid.model.CardInfo
import com.example.riseandroid.model.Ticket
import com.example.riseandroid.ui.screens.Checkout.components.PaymentCardInput
import com.example.riseandroid.ui.screens.Checkout.components.SlidingButton
import com.example.riseandroid.ui.screens.Checkout.components.TicketQuantityInput
import com.example.riseandroid.ui.screens.Checkout.components.TweeDeelButton
import com.example.riseandroid.ui.screens.homepage.ErrorScreen
import com.example.riseandroid.ui.screens.homepage.LoadingScreen
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.forEach
import kotlin.text.all
import kotlin.text.contains
import kotlin.text.forEach
import kotlin.text.isDigit
import kotlin.text.replace
import kotlin.text.toIntOrNull


@Composable
fun CheckoutScreen(
    navController: NavController,
    selectedCinema: String,
    date: String,
    selectedHour: String,
    viewModel: CheckoutViewModel = viewModel(factory = CheckoutViewModel.Factory)
) {
    when (val uiState = viewModel.checkoutUiState) {
        is CheckoutUiState.Loading -> LoadingScreen()
        is CheckoutUiState.Error -> ErrorScreen()
        is CheckoutUiState.Success -> {
            val tickets = uiState.ticketTypes
            val ticketTypes by tickets.collectAsState(initial = emptyList())

            CheckoutDetailScreen(
                ticketTypes = ticketTypes,
                navController = navController,
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
    val selectedTicketQuantities = remember { mutableStateMapOf<Ticket, Int>() }
    var isBuyingTickets by remember { mutableStateOf(true) }
    var cardInfo by remember { mutableStateOf(CardInfo("", "", "", "")) }
    var email by remember { mutableStateOf("") }
    val totalPrice = selectedTicketQuantities.entries.sumOf { (ticketType, quantity) ->
        ticketType.price * quantity
    }

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
            Header(navController)
            Spacer(modifier = Modifier.size(32.dp))
            ToggleTicketOrCard(isBuyingTickets) { isBuyingTickets = !isBuyingTickets }
            Spacer(modifier = Modifier.size(32.dp))
            if (isBuyingTickets) {
                TicketSection(ticketTypes, selectedTicketQuantities)
                Spacer(modifier = Modifier.height(36.dp))
                PaymentSection(cardInfo) { newCardInfo -> cardInfo = newCardInfo }
                Spacer(modifier = Modifier.height(36.dp))
                UserDetailsSection(email) { newEmail -> email = newEmail }
                Spacer(modifier = Modifier.height(36.dp))
                CheckoutButton(totalPrice)
            } else {
                Text("Selecteer Pas", fontSize = 20.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
            }
        }
    }
}

@Composable
fun Header(navController: NavController) {
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
        Text("Checkout", fontSize = 24.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun ToggleTicketOrCard(isBuyingTickets: Boolean, onToggle: () -> Unit) {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        SlidingButton(
            isSelected = isBuyingTickets,
            onToggle = onToggle,
            leftText = "Koop Tickets",
            rightText = "Gebruik Pas"
        )
    }
}

@Composable
fun TicketSection(ticketTypes: List<Ticket>, selectedTicketQuantities: MutableMap<Ticket, Int>) {
    Text("Selecteer Tickets", fontSize = 20.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
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
}

@Composable
fun PaymentSection(cardInfo: CardInfo, onCardAdded: (CardInfo) -> Unit) {
    PaymentCardInput(onCardAdded = onCardAdded, initialCardInfo = cardInfo)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDetailsSection(email: String, onEmailChange: (String) -> Unit) {
    Text("Persoonlijke Details", fontSize = 20.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
    Spacer(modifier = Modifier.height(20.dp))

    Column(modifier = Modifier.fillMaxWidth()) {
        Text("Email", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color.White)
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = email,
            onValueChange = onEmailChange,
            textStyle = TextStyle(color = Color(0xFFB2B5BB), fontSize = 14.sp, fontWeight = FontWeight.Medium),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFFB2B5BB),
                unfocusedBorderColor = Color(0xFFB2B5BB).copy(alpha = 0.5f),
                cursorColor = Color(0xFFB2B5BB)
            ),
            shape = RoundedCornerShape(10.dp)
        )
    }
}

@Composable
fun CheckoutButton(totalPrice: Double) {
    TweeDeelButton(price = totalPrice)
}






