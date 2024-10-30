package com.example.riseandroid.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.auth0.android.Auth0
import com.example.riseandroid.model.Movie
import com.example.riseandroid.ui.screens.account.AccountPage
import com.example.riseandroid.ui.screens.account.AuthViewModel
import com.example.riseandroid.ui.screens.homepage.Homepage
import com.example.riseandroid.ui.screens.login.ForgotPasswordScreen
import com.example.riseandroid.ui.screens.login.ForgotPasswordViewModel
import com.example.riseandroid.ui.screens.login.LoginScreen
import com.example.riseandroid.ui.screens.movieDetail.MovieDetailScreen
import com.example.riseandroid.ui.screens.movieDetail.MovieDetailViewModel
import com.example.riseandroid.ui.screens.scanner.ScanCodeScreen
import com.example.riseandroid.ui.screens.signup.SignUp
import com.example.riseandroid.ui.screens.ticket.TicketsScreen
import com.example.riseandroid.ui.screens.watchlist.WatchlistScreen
import com.example.riseandroid.ui.screens.watchlist.WatchlistViewModel


@Composable
fun BottomNavGraph(
    navController: NavHostController,
    account: Auth0,
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel,
    forgotPasswordViewModel: ForgotPasswordViewModel,
    allMovies: List<Movie>,
    watchlistViewModel: WatchlistViewModel,
) {
    val context = LocalContext.current

    NavHost(
        navController = navController,
        startDestination = BottomBarScreen.Home.route,
        modifier = modifier
    ) {

        composable(route = BottomBarScreen.Home.route) {
            Homepage(navController = navController) // WEIZIGEN
        }
        composable(route = BottomBarScreen.ScanCode.route) {
            ScanCodeScreen(navController = navController)
        }
        composable(route = BottomBarScreen.Tickets.route) {
            TicketsScreen(navController = navController)
        }
        composable(route = BottomBarScreen.Account.route) {
            AccountPage(
                navController = navController,
                context = context,
                authViewModel = authViewModel
            )
        }

        composable(route = "login") {
            LoginScreen(
                navController = navController,
                login = { credentials ->
                    println("Logged in with credentials: $credentials")
                },
                modifier = Modifier.fillMaxSize(),
                authViewModel = authViewModel
            )
        }

        composable(route = "signup") {
            SignUp(
                signUp = { credentials ->
                    println("Signed up with credentials: $credentials")
                },
                modifier = Modifier.fillMaxSize(),
                authViewModel = authViewModel,
                navController = navController
            )
        }

        composable(route = "account") {
            AccountPage(
                navController = navController,
                context = context,
                authViewModel = authViewModel
            )
        }

        composable(route = "forgotPassword") {
            ForgotPasswordScreen(
                modifier = modifier,
                viewModel = forgotPasswordViewModel,
                navController = navController
            )
        }

        composable("movieDetail/{movieId}") { backStackEntry ->
            val movieId = backStackEntry.arguments?.getString("movieId")?.toLongOrNull()
            if (movieId != null) {
                MovieDetailScreen(
                    movieId = movieId,
                    navController = navController,
                    viewModel = viewModel<MovieDetailViewModel>(factory = MovieDetailViewModel.provideFactory(movieId)),
                    watchlistViewModel = watchlistViewModel,
                    authViewModel = authViewModel
                )
            }
        }

        composable("watchlist/{email}") { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email")
            if (email != null) {
                WatchlistScreen(
                    viewModel = watchlistViewModel,
                    allMovies = allMovies,
                    onMovieClick = { movieId ->
                        navController.navigate("movieDetail/$movieId")
                    },
                    navController = navController
                )
            }
        }
    }
}



