package com.example.riseandroid.ui.screens.account

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.riseandroid.ui.screens.signUp.AuthState
import com.example.riseandroid.ui.screens.signUp.AuthViewModel

@Composable
fun AccountPage(
    context: Context,
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    var notificationsEnabled by remember { mutableStateOf(false) }

    val authState by authViewModel.authState.collectAsState()
    val email by authViewModel.email.collectAsState() // E-mail ophalen van AuthViewModel

    LaunchedEffect(authState) {
        if (authState is AuthState.Unauthenticated) {
            navController.navigate("login")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Account",
            style = MaterialTheme.typography.headlineMedium,
            fontSize = 32.sp,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Image(
            painter = rememberAsyncImagePainter("https://example.com/path/to/profile_image.jpg"),
            contentDescription = "Profile Image",
            modifier = Modifier
                .size(100.dp)
                .padding(bottom = 16.dp)
        )

        Text(text = email ?: "Geen e-mail beschikbaar", fontSize = 24.sp) // E-mail weergeven

        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = { /* actie voor tickets */ }, modifier = Modifier.fillMaxWidth()) {
            Text("Mijn Tickets")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { /* actie voor beurten kaart */ }, modifier = Modifier.fillMaxWidth()) {
            Text("10 Beurten Kaart")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { /* actie voor watchlist */ }, modifier = Modifier.fillMaxWidth()) {
            Text("Watchlist")
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = notificationsEnabled,
                onCheckedChange = { notificationsEnabled = it }
            )
            Text("Ik wil graag notificaties ontvangen voor nieuwe films en events")
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                authViewModel.logout()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Uitloggen")
        }
    }
}



