// AccountPage.kt
package com.example.riseandroid.ui.screens.account

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.riseandroid.R
import com.example.riseandroid.ui.theme.ThemeToggle
import com.example.riseandroid.ui.theme.ThemeViewModel


@Composable
fun AccountPage(
    context: Context,
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    // Obtain the shared ThemeViewModel scoped to the Activity
    val themeViewModel: ThemeViewModel = viewModel()

    // Collect the theme state as Compose State
    val isDarkTheme by themeViewModel.isDarkTheme.collectAsState()

    var notificationsEnabled by remember { mutableStateOf(false) }

    val authState by authViewModel.authState.collectAsState()
    val email by authViewModel.email.collectAsState()

    LaunchedEffect(authState) {
        if (authState is AuthState.Unauthenticated) {
            navController.navigate("login") {
                popUpTo("account") { inclusive = true }
            }
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
        )

        Image(
            painter = painterResource(R.drawable.account),
            contentDescription = "Profile Image",
            modifier = Modifier
                .size(120.dp)
                .padding(bottom = 16.dp)
        )

        Text(text = email ?: "Geen e-mail beschikbaar", fontSize = 24.sp)

        Spacer(modifier = Modifier.weight(1f))

        Spacer(modifier = Modifier.height(32.dp))

        // Integrate ThemeToggle with Switch
        ThemeToggle(
            isDarkTheme = isDarkTheme,
            onThemeChange = { isDark ->
                themeViewModel.toggleTheme(isDark)
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
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
