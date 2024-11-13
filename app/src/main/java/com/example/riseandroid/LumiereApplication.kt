package com.example.riseandroid

import android.app.Application
import com.example.riseandroid.data.AppContainer
import com.example.riseandroid.data.DefaultAppContainer
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.riseandroid.notifications.NotificationReceiver
import java.util.Calendar
import android.app.NotificationManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat


class LumiereApplication : Application() {
    /** AppContainer instance used by the rest of classes to obtain dependencies */
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(context = applicationContext)
    }

    fun scheduleNotification(context: Context, movieId: Long, movieName: String, location: String, date: String, dateTime: Calendar) {
        val intent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra("MOVIE_ID", movieId)
            putExtra("MOVIE_NAME", movieName)
            putExtra("LOCATION", location)
            putExtra("DATE", date)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            movieId.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, dateTime.timeInMillis, pendingIntent)
    }



    fun displayImmediateNotification(context: Context, movieId: Long, movieName: String, location: String, date: String) {
        if (NotificationManagerCompat.from(context).areNotificationsEnabled()) {
            val notification = NotificationCompat.Builder(context, "REMINDER_CHANNEL")
                .setSmallIcon(R.drawable.ic_launcher_foreground) // Replace with your app's icon
                .setContentTitle("Reminder for $movieName")
                .setContentText("Your movie at $location on $date is coming up soon.")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build()

            with(NotificationManagerCompat.from(context)) {
                notify(movieId.toInt(), notification)
            }
        } else {
            println("Notifications are not enabled on this device.") // Log if notifications are disabled
        }
    }




}

