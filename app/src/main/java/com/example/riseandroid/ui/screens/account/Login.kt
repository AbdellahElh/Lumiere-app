package com.example.riseandroid.ui.screens.account

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.riseandroid.ui.theme.RiseAndroidTheme
import androidx.compose.material3.ButtonDefaults
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController


class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RiseAndroidTheme {
                LoginScreen()
            }
        }
    }
}

@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel = viewModel(),
    navController: NavHostController? = null
) {
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
        Text(text = "Login", style = MaterialTheme.typography.headlineMedium, color = Color.White)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { loginViewModel.onUsernameChange(it) },
            label = { Text(text = "Username", color = Color.White) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { loginViewModel.onPasswordChange(it) },
            label = { Text(text = "Password", color = Color.White) },
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
            text = "Forgot password?",
            color = Color.White,
            modifier = Modifier.clickable {
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Don't have an account yet? Sign up",
            color = Color.White,
            modifier = Modifier.clickable {
            }
        )

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = { loginViewModel.login() },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            )
        ) {
            Text(text = "Login")
        }
    }
}

@Preview()
@Composable
fun LoginScreenPreview() {
    val navController = rememberNavController()
    RiseAndroidTheme {
        LoginScreen(navController = navController)
    }
}
