package com.example.riseandroid

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.room.Room
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationAPIClient
import com.auth0.android.authentication.storage.CredentialsManager
import com.auth0.android.authentication.storage.SharedPreferencesStorage
import com.example.riseandroid.data.AppContainer
import com.example.riseandroid.data.DefaultAppContainer
import com.example.riseandroid.data.RiseDatabase
import com.example.riseandroid.data.entitys.watchlist.UserManager
import com.example.riseandroid.network.WatchlistApi
import com.example.riseandroid.network.auth0.Auth0Api
import com.example.riseandroid.notifications.NotificationReceiver
import com.example.riseandroid.repository.Auth0Repo
import com.example.riseandroid.repository.WatchlistRepo
import com.example.riseandroid.ui.screens.account.AuthViewModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Calendar


class LumiereApplication : Application() {
    val userManager = UserManager()
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()

        container = DefaultAppContainer(
            context = this,
            userManager = userManager
        )
    }

    fun scheduleNotification(context: Context, movieId: Int, movieName: String, location: String, date: String, dateTime: Calendar) {
        val intent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra("MOVIE_ID", movieId)
            putExtra("MOVIE_NAME", movieName)
            putExtra("LOCATION", location)
            putExtra("DATE", date)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            movieId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = context.getSystemService(ALARM_SERVICE) as AlarmManager
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, dateTime.timeInMillis, pendingIntent)
    }



    fun displayImmediateNotification(context: Context, movieId: Int, movieName: String, location: String, date: String, time: String) {
        if (NotificationManagerCompat.from(context).areNotificationsEnabled()) {
            val notificationText = "Your movie at $location on $date at $time is coming up soon."

            val notification = NotificationCompat.Builder(context, "REMINDER_CHANNEL")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Reminder for $movieName")
                .setContentText(notificationText)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build()

            with(NotificationManagerCompat.from(context)) {
                notify(movieId, notification)
            }
        } else {
            println("Notifications are not enabled on this device.")
        }
    }
}





