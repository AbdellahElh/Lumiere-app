package com.example.riseandroid.ui.screens.account

import android.content.Context
import android.widget.Toast
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.riseandroid.R

@Composable
fun AccountPage(
    context: Context,
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    var notificationsEnabled by remember { mutableStateOf(false) }

    val authState by authViewModel.authState.collectAsState()
    val email by authViewModel.email.collectAsState()

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
            modifier = Modifier.padding(bottom = 32.dp),
            color = Color.White
        )

        Image(
            painter = painterResource(R.drawable.account),
            contentDescription = "Profile Image",
            modifier = Modifier
                .size(120.dp)
                .padding(bottom = 16.dp)
        )

        Text(text = email ?: "Geen e-mail beschikbaar", fontSize = 24.sp, color = Color.White)

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                if (authState is AuthState.Authenticated && email != null) {
                    navController.navigate("watchlist/${email}")
                } else {
                    Toast.makeText(context, "U moet ingelogd zijn om de watchlist te bekijken", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Watchlist")
        }

        Spacer(modifier = Modifier.weight(1f))

        Spacer(modifier = Modifier.height(32.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = notificationsEnabled,
                onCheckedChange = { notificationsEnabled = it }
            )
            Text("Ik wil graag notificaties ontvangen voor nieuwe films en events", color = Color.White)
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



