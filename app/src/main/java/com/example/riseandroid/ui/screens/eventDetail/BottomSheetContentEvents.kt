package com.example.riseandroid.ui.screens.eventDetail

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.se.omapi.Session
import android.util.Log
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
import com.example.riseandroid.data.entitys.Tickets.TicketEntity
import com.example.riseandroid.model.EventModel
import com.example.riseandroid.ui.screens.movieDetail.components.EmailSender
import com.example.riseandroid.ui.screens.ticket.TicketViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Properties
import javax.mail.PasswordAuthentication
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage


@Composable
fun BottomSheetContentEvents(
    ticketViewModel : TicketViewModel,
    eventId: Int,
    cinemas: List<Cinema>,
    context: Context,
    navController: NavController,
    event: EventModel,
    email: String,
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
                        onCheckoutEvent(ticketViewModel , eventId, selectedCinema, selectedDate, selectedTime, event,  context, email)
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
    ticketViewModel: TicketViewModel,
    eventId: Int,
    selectedCinema: String,
    date: String,
    selectedTime: String,
    event: EventModel,
    context: Context,
    email: String
    ) {
    val url = when (selectedCinema) {
        "Brugge" -> "https://tickets.lumierecinema.be/lumiere/nl/flow_configs/webshop/steps/start/show/${event.id}"
        "Antwerpen" -> "https://tickets.lumiere-antwerpen.be/lumiereantwerpen/nl/flow_configs/webshop/steps/start/show/${event.id}"
        "Mechelen" -> "https://tickets.lumieremechelen.be/lumieremechelen/nl/flow_configs/webshop/steps/start/show/${event.id}"
        "Cinema Cartoons" -> "https://tickets.cinemacartoons.be/cartoons/nl/flow_configs/webshop/steps/start/show/${event.id}"
        else -> ""
    }
    val showtime = "$date" + "T" + "$selectedTime" + ":00"
    if (url.isNotEmpty()) {
        ticketViewModel.addTicket(0, eventId, selectedCinema, showtime) { ticket ->
            if (ticket != null) {
                val emailSender = EmailSender(
                    username = "rise6698@gmail.com",
                    password = "zkuq squo tgzz kriv"
                )
                val ticketID = ticket.id;
                var ticketType = "";
                var price = 0.0 ;
                if(ticket.type == 0){
                    price = 12.00;
                    ticketType = "Standaard"
                }else if(ticket.type == 1){
                    price = 11.5;
                    ticketType = "Senior"

                }else if(ticket.type == 2){
                    price = 10.00
                    ticketType = "Student"

                }else{
                    price = .00;
                    ticketType = "Andere"

                }


                val emailBody = """
                <p>Beste Lumiere {Location} bezoeker, <br/><br/>
                Bedankt voor je aankoop. De betaling voor je bestelling met nummer {Id} is ontvangen en verwerkt. <br/>
                Je kan je e-tickets via de volgende link openen:</p>
                <a href='https://localhost:5001/tickets/{Id}'>Open je ticket</a>
                <p>Je hoeft ze niet af te drukken, je kan ze gewoon op je smartphone laten zien aan de ingang van de cinema.</p>
                <h4>Instructies:</h4>
                <ul>
                    <li>Noteer veiligheidshalve het bestelnummer.</li>
                    <li>Neem je ticket mee naar de voorstelling.</li>
                    <li>Gelieve je ticket te tonen aan de medewerker bij het binnenkomen van de cinema. Indien de medewerker niet aanwezig is dan zal de kassamedewerker je ticket valideren. In beide gevallen mag je op vertoon en na scan van je ticket de cinema binnen</li>
                </ul>
                <h2>Info Tickets:</h2>
                <h3>{title}</h3>
                <p>{DateTime }</p>
                <p>1X {Type}:{Price}€</p>
                <p>Totaal: {Price}€</p>
                <p><br/>Veel plezier bij de voorstelling! <br/><br/> vriendelijke groet, <br/><br/> het team van stadsbioscoop Lumiere {Location}</p>
            """.trimIndent()
                emailBody.replace("{Location}", selectedCinema)
                emailBody.replace("{Id}", ticketID.toString())
                emailBody.replace("{Title}", event.title)
                emailBody.replace("{Type}", ticketType)
                emailBody.replace("{Price}", price.toString())



                emailSender.sendEmail(
                    to = email,
                    subject = "Bevestiging van uw aankoop bij stadsbioscoop Lumiere {selectedCinema}",
                    body = emailBody
                )

            }
            }
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    } else {
        Toast.makeText(context, "Geen geldige URL gevonden", Toast.LENGTH_SHORT).show()
    }
}

