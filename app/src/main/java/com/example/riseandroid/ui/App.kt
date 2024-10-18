package com.example.riseandroid.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.riseandroid.navigation.BottomBar
import com.example.riseandroid.navigation.NavHostWrapper
import com.example.riseandroid.ui.screens.homepage.Homepage
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.riseandroid.navigation.BottomBar
import com.example.riseandroid.navigation.NavHostWrapper
import com.example.riseandroid.screens.AccountScreen
import com.example.riseandroid.screens.ScanCodeScreen
import com.example.riseandroid.screens.TicketsScreen
import com.example.riseandroid.screens.homepage.Homepage
import com.example.riseandroid.screens.movieDetail.MovieDetailScreen

@Composable
fun LumiereApp() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomBar(navController = navController) }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "home",
            Modifier.padding(paddingValues)
        ) {
            composable("home") {
                Homepage(navController = navController)
            }
            composable("qrScan") {
                ScanCodeScreen(navController = navController)
            }
            composable("tickets") {
                TicketsScreen(navController = navController)
            }
            composable("account") {
                AccountScreen(navController = navController)
            }
            composable("movieDetail/{movieId}") { backStackEntry ->
                val movieId = backStackEntry.arguments?.getString("movieId")?.toLongOrNull()
                MovieDetailScreen(movieId = movieId, navController = navController)
            }
        }
    }
}







