package com.example.riseandroid.navigation


import androidx.compose.ui.graphics.vector.ImageVector
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.*


sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Home : BottomBarScreen(
        route = "home",
        title = "Home",
        icon =Lucide.House

    )


    object Tickets : BottomBarScreen(
        route = "tickets",
        title = "Tickets",
        icon = Lucide.Tickets
    )
    object Account : BottomBarScreen(
        route = "login",
        title = "Account",
        icon = Lucide.CircleUserRound
    )

}