package com.example.riseandroid.ui.screens.eventDetail

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.riseandroid.data.entitys.Cinema
import com.example.riseandroid.model.EventModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetContentEvents(
    cinemas: List<Cinema>,
    context: Context,
    navController: NavController,
    event: EventModel,
    onDismiss: () -> Unit
) {
    var selectedCinema by remember { mutableStateOf(cinemas.firstOrNull()?.name ?: "") }
    var selectedDate by remember { mutableStateOf("") }
    var selectedTime by remember { mutableStateOf("") }
    val showtimes = cinemas.find { it.name == selectedCinema }?.showtimes ?: emptyList()
    val availableDates = showtimes.map { it.split(" ")[0] }.distinct()

    LaunchedEffect(selectedCinema) {
        if (availableDates.isNotEmpty()) {
            selectedDate = availableDates.first()
            selectedTime = showtimes.firstOrNull { showtime -> showtime.startsWith(selectedDate) }
                ?.split(" ")?.get(1) ?: ""
        }
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color(0xFFE5CB77)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            DropdownMenuWithLabel(
                label = "Cinema",
                options = cinemas.map { cinema -> cinema.name },
                selectedOption = selectedCinema,
                onOptionSelected = { selectedCinemaName ->
                    selectedCinema = selectedCinemaName
                    selectedDate = ""
                    selectedTime = ""
                }
            )
            DropdownMenuWithLabel(
                label = "Datum",
                options = availableDates,
                selectedOption = selectedDate,
                onOptionSelected = { selectedDateOption ->
                    selectedDate = selectedDateOption
                    selectedTime = showtimes.firstOrNull { showtime -> showtime.startsWith(selectedDate) }
                        ?.split(" ")?.get(1) ?: ""
                }
            )
            DropdownMenuWithLabel(
                label = "Tijd",
                options = showtimes.filter { showtime -> showtime.startsWith(selectedDate) }
                    .map { showtime -> showtime.split(" ")[1] },
                selectedOption = selectedTime,
                onOptionSelected = { selectedTimeOption -> selectedTime = selectedTimeOption }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    onCheckoutEvent(selectedCinema, selectedDate, selectedTime, event, navController, context)
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B1E25)),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(vertical = 18.dp)
            ) {
                Text(
                    text = "Ga Verder",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun DropdownMenuWithLabel(
    label: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                .clickable { expanded = true }
                .padding(8.dp)
        ) {
            Text(text = selectedOption.ifEmpty { "Selecteer $label" })
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    },
                    text = { Text(option) }
                )
            }
        }
    }
}

fun onCheckoutEvent(
    selectedCinema: String,
    date: String,
    selectedTime: String,
    event: EventModel,
    navController: NavController,
    context: Context
) {
    val url = when (selectedCinema) {
        "Brugge" -> "https://tickets.lumierecinema.be/lumiere/nl/flow_configs/webshop/steps/start/show/${event.id}"
        "Antwerpen" -> "https://tickets.lumiere-antwerpen.be/lumiereantwerpen/nl/flow_configs/webshop/steps/start/show/${event.id}"
        "Mechelen" -> "https://tickets.lumieremechelen.be/lumieremechelen/nl/flow_configs/webshop/steps/start/show/${event.id}"
        "Cinema Cartoons" -> "https://tickets.cinemacartoons.be/cartoons/nl/flow_configs/webshop/steps/start/show/${event.id}"
        else -> ""
    }

    if (url.isNotEmpty()) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    } else {
        Toast.makeText(context, "Geen geldige URL gevonden", Toast.LENGTH_SHORT).show()
    }
}

