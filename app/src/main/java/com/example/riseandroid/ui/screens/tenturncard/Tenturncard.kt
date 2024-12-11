import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.util.TableInfo
import com.example.riseandroid.model.Tenturncard
import com.example.riseandroid.repository.TenturncardRepository
import com.example.riseandroid.ui.screens.account.AuthViewModel
import com.example.riseandroid.ui.screens.homepage.HomepageViewModel
import com.example.riseandroid.ui.screens.ticket.generateQRCode
import java.util.prefs.NodeChangeEvent

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
                    modifier = Modifier.fillMaxSize().padding(16.dp)
                ) {
                    items(cards) { card ->
                        TenturnCardItem(
                            card = card,
                            onQrCodeClick = { cardId ->
                                tenTurnCardViewModel.updateCardById(cardId)
                            }
                        )
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
fun TenturnCardItem(card: Tenturncard,onQrCodeClick: (Int) -> Unit) {
    Card(
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .padding(16.dp)
            .height(400.dp) // Extra hoogte voor de QR-code
            .width(250.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
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
                text = if (card.expirationDate.isEmpty()) "-" else formatDate(card.expirationDate),
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 20.sp),
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Beurten over:",
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 20.sp, fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Text(
                text = "${card.amountLeft}",
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 20.sp),
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

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
                        .clickable { onQrCodeClick(card.id) }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { /* Voeg functionaliteit toe voor bewerken */ },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(
                    text = "Bewerken",
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}


