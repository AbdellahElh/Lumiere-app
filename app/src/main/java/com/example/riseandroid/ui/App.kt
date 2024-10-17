package com.example.riseandroid.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.riseandroid.navigation.BottomBar
import com.example.riseandroid.navigation.NavHostWrapper
import com.example.riseandroid.ui.screens.homepage.Homepage

@Composable
fun LumiereApp() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomBar(navController = navController) }
    ) { paddingValues ->
        NavHostWrapper(navController = navController, paddingValues = paddingValues)
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Homepage(
                contentPadding = paddingValues
            )
        }
    }
}







