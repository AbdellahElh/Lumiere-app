package com.example.riseandroid.screens

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
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun TicketsScreen(navController: NavHostController? = null) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Cyan),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Tickets",
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
    }
}

@Composable
@Preview
fun TicketsScreenPreview() {
    val navController = rememberNavController()

    TicketsScreen(navController = navController)
}