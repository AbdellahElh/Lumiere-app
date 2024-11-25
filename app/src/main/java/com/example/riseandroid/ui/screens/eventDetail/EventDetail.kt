package com.example.riseandroid.ui.screens.eventDetail

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.riseandroid.ui.screens.homepage.ErrorScreen
import com.example.riseandroid.ui.screens.homepage.LoadingScreen


@Composable
fun EventDetailScreen(
    eventId: Int,
    navController: NavController,
    viewModel: EventDetailViewModel = viewModel(factory = EventDetailViewModel.provideFactory(eventId)),
) {
    when (val uiState = viewModel.eventDetailUiState) {
        is EventDetailUiState.Loading -> LoadingScreen()
        is EventDetailUiState.Error -> ErrorScreen()
        is EventDetailUiState.Success -> {
            val event = uiState.specificEvent

            EventDetailContent(
                event = event,
                navController = navController
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailContent(
    event: EventModel,
    navController: NavController,
) {
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
                    onBackClick = { navigateBack(navController) }
                )
                Spacer(modifier = Modifier.height(32.dp))
                EventPoster(event)
                Spacer(modifier = Modifier.height(20.dp))
                EventInfo(event)
                Spacer(modifier = Modifier.height(10.dp))
                EventDescription(event)
                Spacer(modifier = Modifier.height(35.dp))
                VisitEventButton(event)
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
                .clickable { onBackClick() }
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
        text = event.title ?: "Onbekend evenement",
        fontSize = 28.sp,
        fontWeight = FontWeight.Medium,
        color = Color.White
    )

    Spacer(modifier = Modifier.height(10.dp))

    Text(
        text = "Genre: ${event.genre}",
        fontSize = 16.sp,
        color = Color(0xFFBABFC9)
    )

    Spacer(modifier = Modifier.height(8.dp))

    Text(
        text = "Regisseur: ${event.director}",
        fontSize = 14.sp,
        color = Color(0xFFBABFC9)
    )
}

@Composable
fun EventDescription(event: EventModel) {
    Text(
        text = "Beschrijving",
        fontSize = 28.sp,
        fontWeight = FontWeight.Medium,
        color = Color.White,
        modifier = Modifier.padding(top = 14.dp)
    )

    Text(
        text = event.description ?: "Geen beschrijving beschikbaar",
        fontSize = 15.sp,
        fontWeight = FontWeight.Light,
        color = Color(0xFF696D74),
        modifier = Modifier.padding(top = 16.dp)
    )
}

@Composable
fun VisitEventButton(event: EventModel) {
    val context = LocalContext.current
    val eventLink = event.eventLink ?: "" // Stel een lege string in als default
    Button(
        onClick = {
            if (eventLink.isNotEmpty()) {
                visitEventLink(eventLink, context)
            } else {
                Toast.makeText(context, "Event link is niet beschikbaar", Toast.LENGTH_SHORT).show()
            }
        },
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFE5CB77),
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(12.dp),
        contentPadding = PaddingValues(vertical = 15.dp)
    ) {
        Text(
            text = "Bezoek Evenement",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )
    }
}


fun visitEventLink(link: String, context: Context) {
    try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "Kan de link niet openen", Toast.LENGTH_SHORT).show()
    }
}

fun navigateBack(navController: NavController, targetRoute: String? = null) {
    if (targetRoute != null) {
        navController.navigate(targetRoute) {
            popUpTo(navController.graph.startDestinationId) { inclusive = false }
        }
    } else {
        navController.popBackStack()
    }
}



