package com.example.riseandroid.ui.screens.movieDetail.components

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
import com.example.riseandroid.LumiereApplication
import com.example.riseandroid.data.entitys.event.AddTicketDTO
import com.example.riseandroid.network.ResponseCinema
import com.example.riseandroid.network.ResponseMovie
import com.example.riseandroid.ui.screens.ticket.TicketViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetContent(
    ticketViewModel: TicketViewModel,
    movieId: Int,
    cinemas: List<ResponseCinema>,
    context: Context,
    movie: ResponseMovie,
    onDismiss: () -> Unit

) {
    var selectedCinema by remember { mutableStateOf(cinemas.firstOrNull()?.name ?: "") }
    var selectedDate by remember { mutableStateOf("") }
    var selectedTime by remember { mutableStateOf("") }

    // Filter de showtimes van de geselecteerde cinema
    val showtimes = cinemas.find { it.name == selectedCinema }?.showtimes ?: emptyList()

    // Verkrijg unieke datums van showtimes
    val availableDates = showtimes.map { it.split("T")[0] }.distinct()

    // Filter tijden op basis van geselecteerde datum
    val availableTimes = showtimes.filter { it.startsWith(selectedDate) }
        .map { it.split("T")[1].substring(0, 5) } // Uur en minuten

    // Logica bij het veranderen van de cinema
    LaunchedEffect(selectedCinema) {
        if (availableDates.isNotEmpty()) {
            selectedDate = availableDates.first()
            selectedTime = availableTimes.firstOrNull().orEmpty()
        } else {
            selectedDate = ""
            selectedTime = ""
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
                    selectedTime = availableTimes.firstOrNull().orEmpty()
                }
            )
            DropdownMenuWithLabel(
                label = "Tijd",
                options = availableTimes,
                selectedOption = selectedTime,
                onOptionSelected = { selectedTimeOption -> selectedTime = selectedTimeOption }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (selectedCinema.isNotEmpty() && selectedDate.isNotEmpty() && selectedTime.isNotEmpty()) {
                        onCheckoutEvent(
                            ticketViewModel,
                            movieId,
                            selectedCinema,
                            selectedDate,
                            selectedTime,
                            movie,
                            context
                        )
                        onDismiss()
                    } else {
                        Toast.makeText(context, "Vul alle velden in voordat u doorgaat", Toast.LENGTH_SHORT).show()
                    }
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
            Text(color = Color.Black, text = selectedOption.ifEmpty { "Selecteer $label" } )
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
    ticketViewModel: TicketViewModel,
    movieId: Int,
    selectedCinema: String,
    date: String,
    selectedTime: String,
    movie: ResponseMovie,
    context: Context
) {

    val url = when (selectedCinema) {
        "Brugge" -> "https://tickets.lumierecinema.be/lumiere/nl/flow_configs/webshop/steps/start/show/${movie.id}"
        "Antwerpen" -> "https://tickets.lumiere-antwerpen.be/lumiereantwerpen/nl/flow_configs/webshop/steps/start/show/${movie.id}"
        "Mechelen" -> "https://tickets.lumieremechelen.be/lumieremechelen/nl/flow_configs/webshop/steps/start/show/${movie.id}"
        "Cinema Cartoons" -> "https://tickets.cinemacartoons.be/cartoons/nl/flow_configs/webshop/steps/start/show/${movie.id}"
        else -> ""
    }
    val showtime = date + "T" + selectedTime + ":00"

    // Parse the showtime into a Calendar object
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
    val showDate: Calendar? = try {
        Calendar.getInstance().apply {
            time = dateFormat.parse(showtime) ?: throw IllegalArgumentException("Invalid date format")
        }
    } catch (e: Exception) {
        println("Error parsing showtime: ${e.message}")
        null
    }

    if (showDate != null) {
        // Calculate notification time (2 days before the show)
        val notificationTime = Calendar.getInstance().apply {
            timeInMillis = showDate.timeInMillis
            add(Calendar.DAY_OF_YEAR, -2)
        }

        val currentTime = Calendar.getInstance()

        // Access LumiereApplication to schedule or display notifications
        val lumiereApp = context.applicationContext as LumiereApplication

        if (notificationTime.before(currentTime)) {
            println("Triggering immediate notification for movieId: ${movie.id}")
            // Trigger immediate notification if the notification time has already passed
            lumiereApp.displayImmediateNotification(
                context,
                movie.id,
                movie.title,
                selectedCinema,
                date,
                selectedTime
            )
        } else {
            println("Scheduling notification for movieId: ${movie.id} at ${notificationTime.time}")
            // Schedule the notification for 2 days before the show
            lumiereApp.scheduleNotification(
                context,
                movie.id,
                movie.title,
                selectedCinema,
                date,
                notificationTime
            )
        }
    } else {
        println("ShowDate is null, notification scheduling skipped")
    }

    if (url.isNotEmpty()) {
        val newTicket =  AddTicketDTO(
            MovieId = movieId,
            EventId = 0,
            CinemaName = selectedCinema,
            ShowTime = showtime
        )
        ticketViewModel.addTicket(newTicket)
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    } else {
        Toast.makeText(context, "Geen geldige URL gevonden", Toast.LENGTH_SHORT).show()
    }
}