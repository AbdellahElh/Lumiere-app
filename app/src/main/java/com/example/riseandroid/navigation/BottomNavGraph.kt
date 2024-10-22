package com.example.riseandroid.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.riseandroid.ui.screens.Checkout.CheckoutScreen
import com.example.riseandroid.ui.screens.account.AccountScreen
import com.example.riseandroid.ui.screens.scanner.ScanCodeScreen
import com.example.riseandroid.ui.screens.ticket.TicketsScreen
import com.example.riseandroid.ui.screens.homepage.Homepage
import com.example.riseandroid.ui.screens.movieDetail.MovieDetailScreen

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
            AccountScreen(navController = navController)
        }

        composable("movieDetail/{movieId}") { backStackEntry ->
            val movieId = backStackEntry.arguments?.getString("movieId")?.toLongOrNull()
            if (movieId != null) {
                MovieDetailScreen(movieId = movieId, navController = navController)
            }
        }
        composable(
            "checkout/{selectedCinema}/{date}/{selectedHour}",
            arguments = listOf(
                navArgument("selectedCinema") { type = NavType.StringType },
                navArgument("date") { type = NavType.StringType },
                navArgument("selectedHour") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val selectedCinema = backStackEntry.arguments?.getString("selectedCinema") ?: ""
            val date = backStackEntry.arguments?.getString("date") ?: ""
            val selectedHour = backStackEntry.arguments?.getString("selectedHour") ?: ""

            CheckoutScreen(navController, selectedCinema, date, selectedHour)
        }

    }
}
