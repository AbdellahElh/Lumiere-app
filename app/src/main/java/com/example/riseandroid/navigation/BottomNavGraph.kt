package com.example.riseandroid.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.riseandroid.screens.AccountScreen
import com.example.riseandroid.screens.ScanCodeScreen
import com.example.riseandroid.screens.TicketsScreen
import com.example.riseandroid.screens.homepage.Homepage

@Composable
fun BottomNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = BottomBarScreen.Home.route,
        modifier = modifier
    ) {
        composable(route = BottomBarScreen.Home.route) {
            Homepage(navController = navController)
        }
        composable(route = BottomBarScreen.ScanCode.route) {
            ScanCodeScreen()
        }
        composable(route = BottomBarScreen.Tickets.route) {
            TicketsScreen()
        }
        composable(route = BottomBarScreen.Account.route) {
            AccountScreen()
        }
    }
}
