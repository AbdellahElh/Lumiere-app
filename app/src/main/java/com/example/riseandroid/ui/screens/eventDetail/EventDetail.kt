package com.example.riseandroid.ui.screens.eventDetail

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.riseandroid.R
import com.example.riseandroid.model.EventModel
import com.example.riseandroid.ui.screens.account.AuthState
import com.example.riseandroid.ui.screens.account.AuthViewModel
import com.example.riseandroid.ui.screens.homepage.ErrorScreen
import com.example.riseandroid.ui.screens.homepage.LoadingScreen
import com.example.riseandroid.ui.screens.movieDetail.NextStepButton
import com.example.riseandroid.ui.screens.movieDetail.navigateBack
import com.example.riseandroid.ui.screens.ticket.TicketViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailScreen(
    eventId: Int,
    navController: NavController,
    viewModel: EventDetailViewModel = viewModel(factory = EventDetailViewModel.provideFactory(eventId)),
    authViewModel: AuthViewModel,
    ticketViewModel: TicketViewModel,
    onBackToEvents: () -> Unit
) {
    val context = LocalContext.current
    val authState by authViewModel.authState.collectAsState()
    val isUserLoggedIn = authState is AuthState.Authenticated


    when (val uiState = viewModel.eventDetailUiState) {
        is EventDetailUiState.Loading -> LoadingScreen()
        is EventDetailUiState.Error -> ErrorScreen()
        is EventDetailUiState.Success -> {
            val event = uiState.specificEvent
            var showBottomSheet by remember { mutableStateOf(false) }

            EventDetailContent(
                event = event,
                navController = navController,
                onReserveClick = {
                    if (isUserLoggedIn) {
                        showBottomSheet = true
                    } else {
                        Toast.makeText(
                            context,
                            "U moet ingelogd zijn om door te gaan naar de volgende stap",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                isUserLoggedIn = isUserLoggedIn,
                onBackClick = { onBackToEvents() }
            )

            if (showBottomSheet) {
                ModalBottomSheet(
                    onDismissRequest = { showBottomSheet = false },
                    sheetState = rememberModalBottomSheetState(),
                    containerColor = Color(0xFFE5CB77)
                ) {
                    BottomSheetContentEvents(
                        ticketViewModel = ticketViewModel,
                        eventId= eventId,
                        cinemas = event.cinemas ?: emptyList(),
                        context = context,
                        event = event,
                        onDismiss = { showBottomSheet = false }
                    )
                }
            }
        }
    }
}

@Composable
fun EventDetailContent(
    event: EventModel,
    navController: NavController,
    onReserveClick: () -> Unit,
    isUserLoggedIn: Boolean,
    onBackClick: () -> Unit = { navigateBack(navController) }
) {
    var isExpanded by remember { mutableStateOf(false) }

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
                EventDetailHeader(
                    navController = navController,
                    onBackClick = onBackClick
                )
                Spacer(modifier = Modifier.height(32.dp))
                EventPoster(event)
                Spacer(modifier = Modifier.height(20.dp))
                EventInfo(event)
                Spacer(modifier = Modifier.height(10.dp))
                EventDescription(event, isExpanded) { isExpanded = !isExpanded }
                Spacer(modifier = Modifier.height(35.dp))
                NextStepButton(
                    isUserLoggedIn = isUserLoggedIn,
                    onClick = onReserveClick,
                )
                Spacer(modifier = Modifier.height(18.dp))
            }
        }
    }
}


@Composable
fun EventDetailHeader(navController: NavController, onBackClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.btn_back),
            contentDescription = "Back",
            modifier = Modifier
                .size(24.dp)
                .clickable {
                    navController.popBackStack()
                    navController.navigate("homepage?selectedTab=2")
                }
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = "Event Details",
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White,
        )
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun EventPoster(event: EventModel) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        contentAlignment = Alignment.Center
    ) {
        if (!event.cover.isNullOrEmpty()) {
            Image(
                painter = rememberAsyncImagePainter(event.cover),
                contentDescription = "Event Poster",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(400.dp)
                    .clip(RoundedCornerShape(20.dp))
            )
        }
    }
}

@Composable
fun EventInfo(event: EventModel) {
    Text(
        text = event.title,
        fontSize = 28.sp,
        fontWeight = FontWeight.Medium,
        color = Color.White
    )

    Spacer(modifier = Modifier.height(10.dp))

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Genre: ${event.genre}",
            fontSize = 14.sp,
            fontWeight = FontWeight.Light,
            color = Color(0xFFBABFC9)
        )
    }

    Spacer(modifier = Modifier.height(14.dp))
}

@Composable
fun EventDescription(event: EventModel, isExpanded: Boolean, onToggleExpand: () -> Unit) {
    Text(
        text = "Beschrijving",
        fontSize = 28.sp,
        fontWeight = FontWeight.Medium,
        color = Color.White,
        modifier = Modifier.padding(top = 14.dp)
    )

    val displayedDescription =
        if (isExpanded) event.description else event.description.take(100)

    Text(
        text = if (isExpanded) displayedDescription else "$displayedDescription...",
        fontSize = 15.sp,
        fontWeight = FontWeight.Light,
        color = Color(0xFF696D74),
        modifier = Modifier.padding(top = 16.dp)
    )

    if (event.description.length > 100) {
        Text(
            text = if (isExpanded) "Lees Minder" else "Lees Meer",
            fontSize = 15.sp,
            fontWeight = FontWeight.Light,
            color = Color(0xFFE5CB77),
            modifier = Modifier
                .clickable { onToggleExpand() }
        )
    }
}




