package com.example.riseandroid

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.riseandroid.navigation.BottomBar
import com.example.riseandroid.navigation.NavHostWrapper
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
            startDestination = "homepage",
            Modifier.padding(paddingValues)
        ) {
            composable("homepage") {
                Homepage(navController = navController)
            }
            composable("movieDetail/{movieId}") { backStackEntry ->
                val movieId = backStackEntry.arguments?.getString("movieId")?.toLongOrNull()
                MovieDetailScreen(movieId = movieId, navController = navController)
            }
        }
    }
}







