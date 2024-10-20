package com.example.riseandroid.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.riseandroid.ui.screens.scanner.ScanCodeScreen
import com.example.riseandroid.ui.screens.ticket.TicketsScreen
import com.example.riseandroid.ui.screens.homepage.Homepage
import com.example.riseandroid.ui.screens.account.LoginScreen

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
            ScanCodeScreen(navController = navController)
        }
        composable(route = BottomBarScreen.Tickets.route) {
            TicketsScreen(navController = navController)
        }
        composable(route = BottomBarScreen.Account.route) {
            LoginScreen(navController = navController)
        }
    }
}
