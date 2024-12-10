package com.example.riseandroid.ui.screens.ticket

import TenturncardScreen
import android.app.Activity
import android.graphics.Bitmap
import android.view.WindowManager
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.Text
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.riseandroid.R
import com.example.riseandroid.model.Tenturncard
import com.example.riseandroid.model.Ticket
import com.example.riseandroid.repository.TenturncardRepository
import com.example.riseandroid.ui.screens.account.AuthState
import com.example.riseandroid.ui.screens.account.AuthViewModel
import com.example.riseandroid.ui.screens.homepage.ErrorScreen
import com.example.riseandroid.ui.screens.homepage.LoadingScreen
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import com.example.riseandroid.ui.screens.homepage.components.SlidingButton
import com.example.riseandroid.ui.screens.movieDetail.MovieDetailContent
import com.example.riseandroid.ui.screens.movieDetail.MovieDetailUiState
import com.example.riseandroid.ui.screens.movieDetail.MovieDetailViewModel

import com.example.riseandroid.ui.screens.watchlist.WatchlistViewModel

@Composable
fun TicketScreen(
    userId: Long,
    authToken: String,
    navController: NavController,
    viewModel: TicketViewModel = viewModel(
        factory = TicketViewModel.provideFactory(userId)
    ),

  //  tenturncardRepository: TenturncardRepository
) {
    when (val uiState = viewModel.ticketUiState) {
        is TicketUiState.Loading -> LoadingScreen()
        is TicketUiState.Error -> ErrorScreen()
        is TicketUiState.Success -> {
            val tickets = uiState.ticketList.collectAsState(initial = emptyList())
            TicketsScreenContent(
                TicketList = tickets.value,


                authToken = authToken!!
            )
        }
    }
}
@Composable
fun TicketsScreenContent(
    TicketList: List<Ticket>,
   authToken: String, // Accept AuthViewModel
) {
    var isTicket by remember { mutableStateOf(true) }
    BrightnessControl(isTicketScreen = true)
    var isEmptyTickets = TicketList.isEmpty()

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        color = MaterialTheme.colorScheme.background
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                Spacer(modifier = Modifier.height(14.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    if (isTicket) {
                        Text(
                            text = "E-Tickets",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.SemiBold,
                        )
                    } else {
                        Text(
                            text = "Beurtenkaarten",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                }
                Spacer(modifier = Modifier.height(32.dp))
                ToggleTicketsOrTenturncards(isTicket) { isTicket = !isTicket }
                Spacer(modifier = Modifier.height(50.dp))
                if (isTicket) {
                    Text(
                        text = "Instructies",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Kom naar de bioscoop, toon en scan de barcode naar de daarvoor bestemde ruimte. Blijf de gezondheidsprotocollen naleven.",
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Light,
                    )
                    Spacer(modifier = Modifier.height(40.dp))
                    if (isEmptyTickets) {
                        Box(
                            modifier = Modifier.fillParentMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Geen Tickets",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.SemiBold,
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(0.85f)
                                    .aspectRatio(1f / 1.584f)
                                    .clip(RoundedCornerShape(15.dp))
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.e_ticket_transparent),
                                    contentDescription = null,
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .rotate(180f)
                                )
                            }
                        }
                    } else {
                        LazyRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(24.dp)
                        ) {
                            TicketList.forEach { ticket ->
                                item {
                                    TicketDetail(
                                        backgroundImage = painterResource(id = R.drawable.e_ticket),
                                        modifier = Modifier.fillParentMaxWidth(0.85f),
                                        ticket
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(44.dp))
                } else {

                    TenturncardScreen()
                }
            }
        }
    }
}

@Composable
fun TicketDetail(backgroundImage: Painter, modifier: Modifier = Modifier, ticket: Ticket) {
    Box(
        modifier = modifier
            .fillMaxWidth(0.85f)
            .aspectRatio(1f / 1.584f)
            .clip(RoundedCornerShape(15.dp))
    ) {
        Image(
            painter = backgroundImage,
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxSize()
                .rotate(180f)
        )

        Column(  modifier = Modifier
            .padding(30.dp)
            .rotate(180f)) {
            Spacer(modifier = Modifier.height(34.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "${ticket.hours}", color = Color.Black, fontSize = 18.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.rotate(180f))
                Text(text = "${ticket.ticketId}", color = Color.Black, fontSize = 18.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.rotate(180f))
            }
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Tijd", color = Color(0xFF717171), fontSize = 18.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.rotate(180f))
                Text("ID", color = Color(0xFF717171), fontSize = 18.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.rotate(180f))
            }
            Spacer(modifier = Modifier.height(34.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "${ticket.date}", color = Color.Black, fontSize = 18.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.rotate(180f))
                Text(text = "${ticket.location}", color = Color.Black, fontSize = 18.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.rotate(180f))
            }
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Datum", color = Color(0xFF717171), fontSize = 18.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.rotate(180f))
                Text("Locatie", color = Color(0xFF717171), fontSize = 18.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.rotate(180f))
            }

            Spacer(modifier = Modifier.height(34.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Film: ${ticket.movie}", color = Color.Black, fontSize = 20.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.rotate(180f))
                Text("e-ticket", color = Color(0xFFF14763), fontSize =20.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.rotate(180f))
            }


            Spacer(modifier = Modifier.weight(1f))
            Box(modifier = (Modifier
                .fillMaxWidth()
                .rotate(180f)), contentAlignment = Alignment.Center) {

                QRCodeScreen(id = ticket.ticketId.toString())
            }

        }
    }
}

fun generateQRCode(id: String): Bitmap {
    val size = 512
    val qrCodeWriter = QRCodeWriter()
    val bitMatrix = qrCodeWriter.encode(id, BarcodeFormat.QR_CODE, size, size)

    val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565)
    for (x in 0 until size) {
        for (y in 0 until size) {
            bitmap.setPixel(x, y, if (bitMatrix[x, y]) Color.Black.toArgb() else Color.White.toArgb())
        }
    }
    return bitmap
}
@Composable
fun QRCodeScreen(id: String) {
    val qrCodeBitmap = generateQRCode(id)
    Image(
        bitmap = qrCodeBitmap.asImageBitmap(),
        contentDescription = "QR Code",
        modifier = Modifier.size(180.dp)
    )
}
@Composable
fun ToggleTicketsOrTenturncards(isTicket: Boolean, onToggle: () -> Unit) {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        SlidingButton(
            isSelected = isTicket,
            onToggle = onToggle,
            leftText = "Tickets",
            rightText = "Beurtenkaarten"
        )
    }
}

@Composable
fun BrightnessControl(isTicketScreen: Boolean) {
    val context = LocalContext.current
    val window = (context as? Activity)?.window

    LaunchedEffect(isTicketScreen) {
        if (isTicketScreen) {
            window?.attributes = window?.attributes?.apply {
                screenBrightness = 1f
            }
        } else {
            window?.attributes = window?.attributes?.apply {
                screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            window?.attributes = window?.attributes?.apply {
                screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE
            }
        }
    }
}
