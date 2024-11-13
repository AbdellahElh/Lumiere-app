package com.example.riseandroid.ui.screens.movieDetail.components

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.DatePicker
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.riseandroid.LumiereApplication
import com.example.riseandroid.model.Movie
import com.example.riseandroid.model.Program
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetContent(
    programList: List<Program>,
    context: Context,
    navController: NavController,
    movie: Movie,
    onDismiss: () -> Unit
) {
    val cinemaLocations = programList.groupBy { it.location }.keys.toList()
    var selectedCinema by remember {
        mutableStateOf(if (cinemaLocations.contains("Brugge")) { "Brugge" } else cinemaLocations.firstOrNull() ?: "")
    }
    var isDropdownExpanded by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf("") }
    var selectedHour by remember { mutableStateOf("") }


    var isTimeDropdownExpanded by remember { mutableStateOf(false) }
    var isDateDropdownExpanded by remember { mutableStateOf(false) }

    val availableDates = programList.filter { it.location == selectedCinema }
        .map { it.date }.distinct().sorted()
    LaunchedEffect(Unit) {

        if (availableDates.isNotEmpty()) {
            selectedDate = availableDates.first()
            val availableHours = getAvailableHours(programList, selectedCinema, selectedDate)
            if (availableHours.isNotEmpty()) {
                selectedHour = availableHours.first()
            }
        }
    }


    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFE5CB77))
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(5.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Cinema",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF1C1F26),
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Box(
                        modifier = Modifier
                            .border(
                                1.dp,
                                Color(0xFF1C1F26).copy(alpha = 0.5f),
                                shape = RoundedCornerShape(10.dp)
                            )
                            .clickable { isDropdownExpanded = !isDropdownExpanded }
                    ) {
                        ExposedDropdownMenuBox(
                            expanded = isDropdownExpanded,
                            onExpandedChange = { isDropdownExpanded = !isDropdownExpanded }
                        ) {
                            TextField(
                                value = selectedCinema,
                                onValueChange = {},
                                readOnly = true,
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = isDropdownExpanded)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor()
                                    .clickable {
                                        isDropdownExpanded = true
                                    },
                                colors = TextFieldDefaults.textFieldColors(
                                    containerColor = Color.Transparent,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent
                                ),
                                textStyle = TextStyle(color = Color.Black)
                            )

                            ExposedDropdownMenu(
                                expanded = isDropdownExpanded,
                                onDismissRequest = { isDropdownExpanded = false }
                            ) {
                                cinemaLocations.forEach { location ->
                                    DropdownMenuItem(
                                        text = { Text(text = location) },
                                        onClick = {
                                            selectedCinema = location
                                            selectedHour = ""
                                            isDropdownExpanded = false

                                            val newAvailableDates = programList.filter { it.location == selectedCinema }
                                                .map { it.date }.distinct().sorted()

                                            if (newAvailableDates.isNotEmpty()) {
                                                selectedDate = newAvailableDates.first()
                                                val availableHours = getAvailableHours(programList, selectedCinema, selectedDate)
                                                if (availableHours.isNotEmpty()) {
                                                    selectedHour = availableHours.first()
                                                }
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Datum",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF1C1F26),
                        modifier = Modifier.padding(bottom = 4.dp)
                    )

                    Box(
                        modifier = Modifier
                            .border(1.dp, Color(0xFF1C1F26).copy(alpha = 0.5f), shape = RoundedCornerShape(10.dp))
                    ) {
                        ExposedDropdownMenuBox(
                            expanded = isDateDropdownExpanded,
                            onExpandedChange = { isDateDropdownExpanded = !isDateDropdownExpanded }
                        ) {
                            println(selectedDate)
                            TextField(
                                value = selectedDate,
                                onValueChange = {},
                                readOnly = true,
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = isDateDropdownExpanded)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor()
                                    .clickable {
                                        isDateDropdownExpanded = true
                                    },
                                colors = TextFieldDefaults.textFieldColors(
                                    containerColor = Color.Transparent,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent
                                ),
                                textStyle = TextStyle(color = Color.Black)
                            )

                            ExposedDropdownMenu(
                                expanded = isDateDropdownExpanded,
                                onDismissRequest = { isDateDropdownExpanded = false }
                            ) {
                                availableDates.forEach { date ->
                                    DropdownMenuItem(
                                        text = { Text(text = date) },
                                        onClick = {
                                            selectedDate = date
                                            isDateDropdownExpanded = false
                                            selectedHour = ""
                                            println("Selected date: ${selectedDate}, Available hours: ${getAvailableHours(programList, selectedCinema, selectedDate)}")

                                            val availableHours = getAvailableHours(programList, selectedCinema, selectedDate)
                                            if (availableHours.isNotEmpty()) {
                                                selectedHour = availableHours.first()
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Tijd",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF1C1F26),
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    val availableHours = getAvailableHours(programList, selectedCinema, selectedDate)

                    Box(
                        modifier = Modifier
                            .border(1.dp, Color(0xFF1C1F26).copy(alpha = 0.5f), shape = RoundedCornerShape(10.dp))
                    ) {
                        ExposedDropdownMenuBox(
                            expanded = isTimeDropdownExpanded,
                            onExpandedChange = { isTimeDropdownExpanded = !isTimeDropdownExpanded }
                        ) {
                            TextField(
                                value = selectedHour,
                                onValueChange = {},
                                readOnly = true,
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = isTimeDropdownExpanded)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor()
                                    .clickable {
                                        isTimeDropdownExpanded = true
                                    },
                                colors = TextFieldDefaults.textFieldColors(
                                    containerColor = Color.Transparent,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent
                                ),
                                textStyle = TextStyle(color = Color.Black)
                            )

                            ExposedDropdownMenu(
                                expanded = isTimeDropdownExpanded,
                                onDismissRequest = { isTimeDropdownExpanded = false }
                            ) {
                                availableHours.forEach { hour ->
                                    DropdownMenuItem(
                                        text = { Text(text = hour) },
                                        onClick = {
                                            selectedHour = hour
                                            isTimeDropdownExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(5.dp))

            Button(
                onClick = {
                    onCheckout(selectedCinema, selectedDate, selectedHour, movie, navController, context)
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B1E25)),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(vertical = 18.dp)
            ) {
                Text(
                    text = "Checkout",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            Spacer(modifier = Modifier.height(1.dp))
        }
    }
}

fun getAvailableHours(programList: List<Program>, selectedCinema: String, selectedDate: String): List<String> {
    return programList
        .filter { it.location == selectedCinema && it.date == selectedDate }
        .flatMap { it.hours.split(",") }
        .distinct()
        .sorted()
}


fun onCheckout(
    selectedCinema: String,
    date: String,
    selectedHour: String,
    movie: Movie,
    navController: NavController,
    context: Context
) {
    val formattedDate = date.replace("/", "-")
    val dateTimeString = "$formattedDate $selectedHour"

    // Parse the date and time into a Calendar object
    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
    val showDate: Calendar? = try {
        Calendar.getInstance().apply {
            time = dateFormat.parse(dateTimeString)
        }
    } catch (e: Exception) {
        println("Error parsing date: ${e.message}")
        null
    }

    if (showDate != null) {
        showDate.let {
            // Create a Calendar instance for the notification time (2 days before)
            val notificationTime = Calendar.getInstance().apply {
                timeInMillis = it.timeInMillis
                add(Calendar.DAY_OF_YEAR, -2) // 2 days before the show date
            }

            val currentTime = Calendar.getInstance()

            if (notificationTime.before(currentTime)) {
                println("Triggering immediate notification for movieId: ${movie.movieId}")
                // If the show is less than 2 days away, trigger the notification immediately
                LumiereApplication().displayImmediateNotification(context, movie.movieId, movie.title, selectedCinema, formattedDate)
            } else {
                println("Scheduling notification for movieId: ${movie.movieId} at ${notificationTime.time}")
                // Schedule the notification for 2 days before the show date
                LumiereApplication().scheduleNotification(context, movie.movieId, movie.title, selectedCinema, formattedDate, notificationTime)
            }
        }
    } else {
        println("showDate is null, notification scheduling skipped")
    }

    // Redirect to the corresponding URL for ticket purchase
    val url = when (selectedCinema) {
        "Brugge" -> "https://tickets.lumierecinema.be/lumiere/nl/flow_configs/webshop/steps/start/show/${movie.movieId}"
        "Antwerpen" -> "https://tickets.lumiere-antwerpen.be/lumiereantwerpen/nl/flow_configs/webshop/steps/start/show/${movie.movieId}"
        "Mechelen" -> "https://tickets.lumieremechelen.be/lumieremechelen/nl/flow_configs/webshop/steps/start/show/${movie.movieId}"
        "Cinema Cartoons" -> "https://tickets.cinemacartoons.be/cartoons/nl/flow_configs/webshop/steps/start/show/${movie.movieId}"
        else -> ""
    }

    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    context.startActivity(intent)
}



fun getCurrentDate(): String {
    val calendar = Calendar.getInstance()
    return "${calendar.get(Calendar.DAY_OF_MONTH)}/${calendar.get(Calendar.MONTH) + 1}/${
        calendar.get(
            Calendar.YEAR
        )
    }"
}
