package com.example.riseandroid.ui.screens.account

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun AccountScreen(
    navController: NavController,authViewModel: AccountViewModel
) {
    val authState by authViewModel.authState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "Account",
            fontSize = 30.sp,
            color = MaterialTheme.colorScheme.onPrimary
        )

        Spacer(modifier = Modifier.height(20.dp))

        if (authState is AuthState.Authenticated) {
            SuccessMessage()
        } else {
            Button(
                onClick = { navController.navigate("signup") },
                modifier = Modifier
                    .width(300.dp)
                    .height(50.dp)
            ) {
                Text("Registreren", fontSize = 20.sp)
            }

        }
    }
}

@Composable
fun SuccessMessage() {
    Column( modifier = Modifier
        .fillMaxSize()
        .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,)
    {
        Text(
            "U bent aangemeld",
            Modifier
                .padding(top = 8.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onPrimary,
            fontSize = 18.sp,
        )

        Spacer(modifier = Modifier.height(40.dp))

        Button(onClick = {},
            modifier = Modifier
                .width(300.dp)
                .height(50.dp)
        ) {
            Text("Uitloggen",fontSize = 20.sp)
        }
    }


}
