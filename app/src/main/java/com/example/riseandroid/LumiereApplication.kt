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


class LumiereApplication : Application() {
    /** AppContainer instance used by the rest of classes to obtain dependencies */
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(context = applicationContext)
    }



    fun scheduleNotification(context: Context, movieId: Long, showDate: Calendar) {
        val reminderTime = Calendar.getInstance().apply {
            timeInMillis = showDate.timeInMillis
            add(Calendar.DAY_OF_YEAR, -2) // 2 days before
        }

        val intent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra("MOVIE_ID", movieId)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context, movieId.toInt(), intent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            reminderTime.timeInMillis,
            pendingIntent
        )
    }

}

