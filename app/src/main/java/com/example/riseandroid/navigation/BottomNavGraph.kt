package com.example.riseandroid.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.riseandroid.ui.screens.account.AccountScreen

import com.auth0.android.Auth0
import com.example.riseandroid.ui.screens.account.AccountPage
import com.example.riseandroid.ui.screens.homepage.Homepage
import com.example.riseandroid.ui.screens.login.ForgotPasswordScreen
import com.example.riseandroid.ui.screens.login.ForgotPasswordViewModel
import com.example.riseandroid.ui.screens.login.LoginScreen
import com.example.riseandroid.ui.screens.scanner.ScanCodeScreen
import com.example.riseandroid.ui.screens.account.AuthViewModel
import com.example.riseandroid.ui.screens.signup.SignUp
import com.example.riseandroid.ui.screens.ticket.TicketsScreen
import com.example.riseandroid.ui.screens.homepage.Homepage
import com.example.riseandroid.ui.screens.movieDetail.MovieDetailScreen


@Composable
fun BottomNavGraph(
    navController: NavHostController,
    account: Auth0,
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel,
    forgotPasswordViewModel: ForgotPasswordViewModel
) {
    // Haal de context buiten de `remember`-scope op
    val context = LocalContext.current

//    // Maak de Auth0Api-instantie met Retrofit aan
//    val authApi: Auth0Api = remember {
//        Retrofit.Builder()
//            .baseUrl("https://alpayozer.eu.auth0.com/")
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//            .create(Auth0Api::class.java)
//    }

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
                MovieDetailScreen(movieId = movieId, navController = navController)
            }
        }


    }
}



