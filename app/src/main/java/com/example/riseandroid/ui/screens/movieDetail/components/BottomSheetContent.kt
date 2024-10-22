package com.example.riseandroid.ui.screens.movieDetail.components

import android.content.Context
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
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetContent(context: Context, navController: NavController, onDismiss: () -> Unit) {
    val cinema = arrayOf("Brugge", "Antwerpen", "Gent", "dsfdsf")
    var selectedCinema by remember { mutableStateOf(cinema[0]) }
    var isDropdownExpanded by remember { mutableStateOf(false) }
    val date = remember { mutableStateOf(getCurrentDate()) }
    val selectedHour = remember { mutableStateOf("12:00") }
    var isTimeDropdownExpanded by remember { mutableStateOf(false) }
    val hours = arrayOf("10:00", "11:00", "12:00", "13:00", "14:00")
    val calendar = Calendar.getInstance()
    val datePickerDialog = remember {
        android.app.DatePickerDialog(
            context,
            { _, year, month, dayOfMonth -> date.value = "$dayOfMonth/${month + 1}/$year" },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
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
            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
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
                            .clickable { datePickerDialog.show() }
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
                                cinema.forEach { item ->
                                    DropdownMenuItem(
                                        text = { Text(text = item) },
                                        onClick = {
                                            selectedCinema = item
                                            isDropdownExpanded = false
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
                            .border(
                                1.dp,
                                Color(0xFF1C1F26).copy(alpha = 0.5f),
                                shape = RoundedCornerShape(10.dp)
                            )
                            .clickable { datePickerDialog.show() }
                    ) {
                        TextField(
                            value = date.value,
                            onValueChange = {},
                            readOnly = true,
                            colors = TextFieldDefaults.textFieldColors(
                                containerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            ),
                            textStyle = TextStyle(color = Color.Black)
                        )
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
                    Box(
                        modifier = Modifier
                            .border(
                                1.dp,
                                Color(0xFF1C1F26).copy(alpha = 0.5f),
                                shape = RoundedCornerShape(10.dp)
                            )
                            .clickable { datePickerDialog.show() }
                    ) {
                        ExposedDropdownMenuBox(
                            expanded = isTimeDropdownExpanded,
                            onExpandedChange = { isTimeDropdownExpanded = !isTimeDropdownExpanded }
                        ) {
                            TextField(
                                value = selectedHour.value,
                                onValueChange = {},
                                readOnly = true,
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = isTimeDropdownExpanded)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { isTimeDropdownExpanded = true },
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
                                hours.forEach { hour ->
                                    DropdownMenuItem(
                                        text = { Text(text = hour) },
                                        onClick = {
                                            selectedHour.value = hour
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
                    onCheckout(selectedCinema, date.value, selectedHour.value, navController)
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1B1E25)
                ),
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

fun onCheckout(
    selectedCinema: String,
    date: String,
    selectedHour: String,
    navController: NavController
) {
    val formattedDate = date.replace("/", "-")
    navController.navigate("checkout/$selectedCinema/$formattedDate/$selectedHour")

}

fun getCurrentDate(): String {
    val calendar = Calendar.getInstance()
    return "${calendar.get(Calendar.DAY_OF_MONTH)}/${calendar.get(Calendar.MONTH) + 1}/${
        calendar.get(
            Calendar.YEAR
        )
    }"
}
