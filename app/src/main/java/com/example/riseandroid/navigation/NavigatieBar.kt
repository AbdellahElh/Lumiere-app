package com.example.riseandroid.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.auth0.android.Auth0
import com.example.riseandroid.LumiereApplication
import com.example.riseandroid.ui.screens.account.AuthViewModel
import com.example.riseandroid.ui.screens.login.ForgotPasswordViewModel
import com.example.riseandroid.ui.screens.watchlist.WatchlistViewModel
import com.example.riseandroid.ui.screens.watchlist.WatchlistViewModelFactory


@Composable
fun NavHostWrapper(
    navController: NavHostController,
    paddingValues: PaddingValues,
    account: Auth0,
    authViewModel: AuthViewModel,
    forgotPasswordViewModel: ForgotPasswordViewModel,
) {
    val context = LocalContext.current
    val application = context.applicationContext as LumiereApplication
    val watchlistViewModel: WatchlistViewModel = viewModel(
        factory = WatchlistViewModelFactory(
            watchlistRepo = application.container.watchlistRepo,
            userManager = application.userManager,
            application = application
        )
    )


    BottomNavGraph(
        navController = navController,
        modifier = Modifier.padding(paddingValues),
        authViewModel = authViewModel,
        forgotPasswordViewModel = forgotPasswordViewModel,
        watchlistViewModel = watchlistViewModel
    )
}


@Composable
fun BottomBar(navController: NavHostController) {
    val screens = listOf(
        BottomBarScreen.Home,
        BottomBarScreen.ScanCode,
        BottomBarScreen.Tickets,
        BottomBarScreen.Watchlist,
        BottomBarScreen.Account
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination


    NavigationBar(  containerColor =   Color(0xFF17191F)) {

        screens.forEach { screen ->
            AddItem(
                screen = screen,
                currentDestination = currentDestination,
                navController = navController
            )
        }
    }
}

@Composable
fun RowScope.AddItem(
    screen: BottomBarScreen,
    currentDestination: NavDestination?,
    navController: NavHostController
) {
    NavigationBarItem(
        label = {
            Text(text = screen.title, color =  Color(0xFFFFFFFF))

        },
        icon = {
            Icon(
                tint=  Color(0xFFFFFFFF),
                imageVector = screen.icon,
                contentDescription = "Navigation Icon for ${screen.title}"
            )
        },
        selected = currentDestination?.hierarchy?.any {
            it.route == screen.route
        } == true,
        colors = NavigationBarItemDefaults.colors(
            indicatorColor = Color.Blue

        ),
        onClick = {
            navController.navigate(screen.route) {
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        }
    )
}