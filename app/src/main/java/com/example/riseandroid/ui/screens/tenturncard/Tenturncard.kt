import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.riseandroid.model.Tenturncard
import com.example.riseandroid.ui.screens.ticket.generateQRCode


@Composable
fun TenturncardScreen(
    tenTurnCardViewModel: TenturncardViewModel = viewModel(factory = TenturncardViewModel.Factory),
) {

    val cards by tenTurnCardViewModel.tenturncards.collectAsState()
    val inputText by tenTurnCardViewModel.inputText.collectAsState()


    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
            inputActivationCodeField(
                inputText = inputText,
                onValueChange = { tenTurnCardViewModel.updateInputText(it) },
                onSubmit = { tenTurnCardViewModel.submitActivationCode(inputText) }
            )
            if (cards.isEmpty()) {
                Text(text = "Loading cards...", style = MaterialTheme.typography.bodyLarge)
            } else {
                LazyRow(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    items(cards) { card ->
                        TenturnCardItem(card, viewModel = tenTurnCardViewModel,  onQrCodeClick = { cardActivationCode ->
                            tenTurnCardViewModel.updateCardById(cardActivationCode)
                        })
                    }
                }
            }
        }
}
fun formatDate(dateString: String): String {
    val dateParts = dateString.substring(0, 10).split("-")
    val showDateFormatted = "${dateParts[0]}-${dateParts[1]}-${dateParts[2]}"
    return showDateFormatted
}

@Composable
fun inputActivationCodeField(
    inputText: String,
    onValueChange: (String) -> Unit,
    onSubmit: () -> Unit
    )
{
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp) // Padding around the entire column
    ) {
        // Input field
        BasicTextField(
            value = inputText,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .background(color = Color.White)
                .testTag("codeInputField"),
            decorationBox = { innerTextField ->
                Box(Modifier.padding(8.dp)) {
                    if (inputText.isEmpty()) {
                        Text(text = "Voer een activatie code in")
                    }
                    innerTextField()
                }
            }
        )

        // Add spacing between the text field and the button
        Spacer(modifier = Modifier.height(16.dp))

        // Submit button
        Button(
            onClick = onSubmit,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .testTag("addBtn")
        ) {
            Text("Voeg een tienbeurtenkaart toe")
        }
    }
}

@Composable
fun TenturnCardItem(card: Tenturncard, viewModel: TenturncardViewModel,onQrCodeClick: (String) -> Unit) {
    Card(
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .padding(16.dp)
            .height(350.dp)
            .width(250.dp)
            .verticalScroll(rememberScrollState()) //Allow scrolling to ensure the CardEditor does not disappear beneath the cardItem

    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
            ,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {

                Text(
                    text = "Tienrittenkaart",
                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 24.sp),
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )


            Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "Vervaldatum:",
                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 20.sp, fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = if (card.expirationDate == "") {
                        "-"
                    } else {
                        formatDate(card.expirationDate)
                    },
                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 20.sp),
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "Aankoopdatum:",
                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 20.sp,fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = if (card.purchaseDate == "") {
                        "-"
                    } else {
                        formatDate(card.purchaseDate)
                    },
                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 20.sp),
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )

                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "Beurten over:",
                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 20.sp,fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            CardEditor(card, viewModel,onQrCodeClick)
        }
    }
}

@Composable
fun CardEditor(card: Tenturncard, viewModel: TenturncardViewModel,onQrCodeClick: (String) -> Unit) {
    val context = LocalContext.current
    var isEditing by remember { mutableStateOf(false) }
    var amountLeftState by remember { mutableStateOf(card.amountLeft.toString()) }
    val uiState = viewModel.tenturncardUiState


    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)) {
        if (!isEditing) {
            Text(
                text = "$amountLeftState",
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 20.sp),
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        } else {
            TextField(
                value = amountLeftState,
                onValueChange = { amountLeftState = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("EditTextField"),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // QR Code Section
        val qrData = "ID: ${card.id} Set AANTALBEURTENOVER -=1"
        val qrCodeBitmap = generateQRCode(qrData)
        if (qrCodeBitmap != null) {
            Image(
                bitmap = qrCodeBitmap.asImageBitmap(),
                contentDescription = "QR Code",
                modifier = Modifier
                    .height(100.dp)
                    .width(100.dp)
                    .clickable { onQrCodeClick(card.ActivationCode) }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (isEditing) {
                    var newAmount = amountLeftState.toIntOrNull()
                    if (newAmount == null) {
                        Toast.makeText(context, "Nieuwe waarde moet een geldig getal zijn", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        viewModel.editTenturncard(
                            card.copy(amountLeft = newAmount )
                        )
                    }
                }
                viewModel.clearToast()
                isEditing = !isEditing // Toggle editing state
            },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("editCardBtn"),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text(
                text = if (isEditing) "Opslaan" else "Bewerken",
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
    // React to state changes
    if (!viewModel.mutableToastMessage.isEmpty()) {
        Toast.makeText(context, viewModel.mutableToastMessage, Toast.LENGTH_SHORT).show()
        amountLeftState = card.amountLeft.toString()
    }
}




