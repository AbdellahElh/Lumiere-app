package com.example.riseandroid.navigation

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.auth0.android.Auth0
import com.example.riseandroid.model.Movie
import com.example.riseandroid.ui.screens.account.AccountPage
import com.example.riseandroid.ui.screens.account.AuthState
import com.example.riseandroid.ui.screens.account.AuthViewModel
import com.example.riseandroid.ui.screens.homepage.Homepage
import com.example.riseandroid.ui.screens.login.ForgotPasswordScreen
import com.example.riseandroid.ui.screens.login.ForgotPasswordViewModel
import com.example.riseandroid.ui.screens.login.LoginScreen
import com.example.riseandroid.ui.screens.movieDetail.MovieDetailScreen
import com.example.riseandroid.ui.screens.movieDetail.MovieDetailViewModel
import com.example.riseandroid.ui.screens.scanner.ScanCodeScreen
import com.example.riseandroid.ui.screens.signup.SignUp
import com.example.riseandroid.ui.screens.ticket.TicketScreen
import com.example.riseandroid.ui.screens.ticket.TicketScreen
import com.example.riseandroid.ui.screens.watchlist.WatchlistScreen
import com.example.riseandroid.ui.screens.watchlist.WatchlistViewModel


@Composable
fun BottomNavGraph(
    navController: NavHostController,
    account: Auth0,
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel = viewModel(factory = AuthViewModel.Factory),
    forgotPasswordViewModel: ForgotPasswordViewModel,
    allMovies: List<Movie>,
    watchlistViewModel: WatchlistViewModel,
) {
    val context = LocalContext.current
    val authState by authViewModel.authState.collectAsState()
    val isUserLoggedIn = authState is AuthState.Authenticated

    val authToken by authViewModel.authToken.collectAsState()
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
            if (!isUserLoggedIn) {
                Dialog(
                    onDismissRequest = { navController.popBackStack() }
                ) {
                    Box(
                        modifier = Modifier
                            .background(Color.White, RoundedCornerShape(8.dp))
                            .padding(16.dp)
                    ) {
                        Column {
                            Text("Login nodig", fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Je moet ingelogd zijn om toegang te krijgen tot je tickets.")
                            Spacer(modifier = Modifier.height(24.dp))
                            Button(onClick = { navController.popBackStack() },  colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFE5CB77),
                                contentColor = Color.White
                            ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("Ok")
                            }
                        }
                    }
                }
            } else {
                TicketScreen(
                    1, navController = navController, authToken = authToken ?: "")
            }
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



