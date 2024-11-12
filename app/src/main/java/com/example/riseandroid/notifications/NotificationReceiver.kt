package com.example.riseandroid.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.riseandroid.R

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val movieId = intent.getLongExtra("MOVIE_ID", -1)
        // Customize your notification message
        val notification = NotificationCompat.Builder(context, "REMINDER_CHANNEL")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Upcoming Movie Reminder")
            .setContentText("Don't forget! Your movie is in two days.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        with(NotificationManagerCompat.from(context)) {
//            Call requires permission which may be rejected by user: code should explicitly
//            check to see if permission is available (with checkPermission) or explicitly handle a potential SecurityException
            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                notify(movieId.toInt(), notification)
            } else {
                // Handle the case where the permission is not granted
            }

        }
    }
}
