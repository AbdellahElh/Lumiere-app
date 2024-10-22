package com.example.riseandroid.ui.screens.Checkout.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.riseandroid.model.CardInfo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentCardDialog(
    onDismiss: () -> Unit,
    onCardAdded: (CardInfo) -> Unit,
    initialCardInfo: CardInfo? = null
) {
    var naam by remember { mutableStateOf(initialCardInfo?.name ?: "") }
    var kaartnummer by remember { mutableStateOf(initialCardInfo?.cardNumber ?: "") }
    var datum by remember { mutableStateOf(initialCardInfo?.expiryDate ?: "") }
    var cvv by remember { mutableStateOf(initialCardInfo?.cvv ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (initialCardInfo?.cardNumber == "") {
                    "Voeg een kaart toe"
                } else {
                    "Pas je kaart aan"
                },
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF111111)
            )
        },
        text = {
            Column {
                OutlinedTextField(
                    value = naam,
                    onValueChange = {
                        if ( it.all { char -> char.isLetter() }) {
                            naam = it
                        }
                    },
                    label = { Text("Naam") },
                    textStyle = TextStyle(color = Color.Gray),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color(0xFFE5CB77),
                        unfocusedBorderColor = Color.Gray,
                        focusedLabelColor = Color(0xFFE5CB77),
                        unfocusedLabelColor = Color.Gray,
                        cursorColor = Color.Gray,

                        )
                )
                Spacer(modifier = Modifier.height(4.dp))

                OutlinedTextField(
                    value = kaartnummer,
                    onValueChange = {
                        if (it.length <= 16 && it.all { char -> char.isDigit() }) {
                            kaartnummer = it
                        }
                    },
                    label = { Text("Kaartnummer") },
                    textStyle = TextStyle(color = Color.Gray),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color(0xFFE5CB77),
                        unfocusedBorderColor = Color.Gray,
                        focusedLabelColor = Color(0xFFE5CB77),
                        unfocusedLabelColor = Color.Gray,
                        cursorColor = Color.Gray,

                        )
                )
                Spacer(modifier = Modifier.height(4.dp))

                OutlinedTextField(
                    value = datum,
                    onValueChange = { newValue ->
                        if (newValue.length <= 5) {
                            datum = when {
                                newValue.length == 2 && !newValue.contains("/") -> "$newValue/"
                                newValue.length == 3 && newValue[2] != '/' -> {
                                    val mm = newValue.substring(0, 2)
                                    val yy = newValue.substring(2)
                                    "$mm/$yy"
                                }
                                newValue.length == 2 && newValue.contains("/") -> newValue.replace("/", "")
                                else -> newValue.replace(Regex("[^0-9/]"), "")
                            }
                        }
                    },
                    label = { Text("Verbruiksdatum (MM/YY)") },
                    textStyle = TextStyle(color = Color.Gray),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color(0xFFE5CB77),
                        unfocusedBorderColor = Color.Gray,
                        focusedLabelColor = Color(0xFFE5CB77),
                        unfocusedLabelColor = Color.Gray,
                        cursorColor = Color.Gray,

                        )
                )
                Spacer(modifier = Modifier.height(4.dp))

                OutlinedTextField(
                    value = cvv,
                    onValueChange = {
                        if (it.length <= 3 && it.all { char -> char.isDigit() }) {
                            cvv = it
                        }
                    },
                    label = { Text("CVV") },
                    textStyle = TextStyle(color = Color.Gray),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color(0xFFE5CB77),
                        unfocusedBorderColor = Color.Gray,
                        focusedLabelColor = Color(0xFFE5CB77),
                        unfocusedLabelColor = Color.Gray,
                        cursorColor = Color.Gray,

                        )
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (kaartnummer.isNotEmpty() && datum.isNotEmpty() && cvv.isNotEmpty() ) {
                        val cardInfo = CardInfo(naam, kaartnummer, datum, cvv)
                        onCardAdded(cardInfo)
                    }
                },
                modifier = Modifier.clip(RoundedCornerShape(12.dp)),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE5CB77))
            ) {
                Text(
                    text = if (initialCardInfo?.cardNumber == "") {
                        "Voeg Toe"
                    } else {
                        "Pas Aan"
                    },
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                modifier = Modifier.clip(RoundedCornerShape(12.dp)),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935))
            ) {
                Text(
                    "Annuleer",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        },
        shape = RoundedCornerShape(16.dp),
        containerColor = Color.White


    )
}