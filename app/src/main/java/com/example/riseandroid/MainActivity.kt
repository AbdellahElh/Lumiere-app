// MainActivity.kt
package com.example.riseandroid

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.auth0.android.Auth0
import com.example.riseandroid.data.AppContainer
import com.example.riseandroid.ui.LumiereApp
import com.example.riseandroid.ui.theme.RiseAndroidTheme
import com.example.riseandroid.ui.theme.ThemeViewModel
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.activity.viewModels
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class MainActivity : ComponentActivity() {

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNotificationChannel(this)
        enableEdgeToEdge()
        setContent {
            // Obtain the ThemeViewModel scoped to the Activity
            val themeViewModel: ThemeViewModel = viewModel()

            val isDarkTheme by themeViewModel.isDarkTheme.collectAsState()

            RiseAndroidTheme(
                darkTheme = isDarkTheme
            ) {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    LumiereApp(
                        themeViewModel = themeViewModel

                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppPreview() {
    RiseAndroidTheme(darkTheme = false) {
        LumiereApp(
            themeViewModel = ThemeViewModel()
        )
    }
}

private fun createNotificationChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = "ReminderChannel"
        val descriptionText = "Channel for Movie Reminders"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel("REMINDER_CHANNEL", name, importance).apply {
            description = descriptionText
        }

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}
