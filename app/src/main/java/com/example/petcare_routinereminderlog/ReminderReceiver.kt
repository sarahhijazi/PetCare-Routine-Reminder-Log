
package com.example.petcare_routinereminderlog

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class ReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        val taskTitle = intent.getStringExtra("taskTitle")
            ?: "Pet care reminder"

        val channelId = "pet_care_reminders"

        // Create notification channel for Android 8 and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Pet Care Reminders",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications for your pet care tasks"
            }

            val manager = context.getSystemService(
                Context.NOTIFICATION_SERVICE
            ) as NotificationManager

            manager.createNotificationChannel(channel)
        }

        // Build the notification
        val notification = NotificationCompat.Builder(
            context,
            channelId
        )
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("🐾 Pet Care Reminder")
            .setContentText(taskTitle)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        // Display the notification
        try {
            NotificationManagerCompat.from(context)
                .notify(System.currentTimeMillis().toInt(), notification)
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }
}
