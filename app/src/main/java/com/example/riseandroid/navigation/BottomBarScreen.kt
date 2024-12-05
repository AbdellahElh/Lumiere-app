package com.example.riseandroid.navigation


import androidx.compose.ui.graphics.vector.ImageVector
import com.composables.icons.lucide.CircleUserRound
import com.composables.icons.lucide.House
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.ScanQrCode
import com.composables.icons.lucide.Star
import com.composables.icons.lucide.Tickets


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

    object ScanCode : BottomBarScreen(
        route = "qrScan",
        title = "Scan",
        icon = Lucide.ScanQrCode
    )
    object Tickets : BottomBarScreen(
        route = "tickets",
        title = "Tickets",
        icon = Lucide.Tickets
    )
    object Watchlist : BottomBarScreen(
        route = "watchlist",
        title = "Watchlist",
        icon = Lucide.Star
    )
    object Account : BottomBarScreen(
        route = "login",
        title = "Account",
        icon = Lucide.CircleUserRound
    )
}