package com.example.riseandroid.ui.screens.account

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.Text
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun AccountScreen(navController: NavHostController? = null) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Blue)
            .semantics{ contentDescription ="Account Screen" },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "ACCOUNT",
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}

@Composable
@Preview
fun AccountScreenPreview() {
    val navController = rememberNavController()
    AccountScreen(navController = navController)
}