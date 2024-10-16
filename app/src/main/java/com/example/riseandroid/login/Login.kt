package com.example.riseandroid.login

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.riseandroid.ui.theme.RiseAndroidTheme

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RiseAndroidTheme {
                // Alleen de loginpagina tonen
                LoginScreen()
            }
        }
    }
}

@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel = viewModel() // Verkrijg de ViewModel
) {
    // Verkrijg de state van de ViewModel
    val username by remember { mutableStateOf(loginViewModel.username.value) }
    val password by remember { mutableStateOf(loginViewModel.password.value) }
    val passwordVisible by remember { mutableStateOf(loginViewModel.passwordVisible.value) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Login", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        // Username Input
        OutlinedTextField(
            value = username,
            onValueChange = { loginViewModel.onUsernameChange(it) },
            label = { Text(text = "Username") },
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Password Input
        OutlinedTextField(
            value = password,
            onValueChange = { loginViewModel.onPasswordChange(it) },
            label = { Text(text = "Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val icon = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                IconButton(onClick = { loginViewModel.onPasswordVisibilityChange() }) {
                    Icon(imageVector = icon, contentDescription = null)
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Don't have an account yet? Sign up",
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.clickable {
                // NOG IMPLEMENTEREN
            }
        )

        Button(
            onClick = { loginViewModel.login() },  // Trigger login via ViewModel
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Login")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    RiseAndroidTheme {
        LoginScreen()
    }
}
