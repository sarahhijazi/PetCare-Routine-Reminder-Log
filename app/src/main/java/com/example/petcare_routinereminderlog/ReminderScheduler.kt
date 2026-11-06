
package com.example.petcare_routinereminderlog

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent

object ReminderScheduler {

    private fun getRequestCode(
        careTaskId: Long,
        reminderDateTime: Long
    ): Int {
        return "$careTaskId-$reminderDateTime".hashCode()
    }

    fun scheduleReminder(
        context: Context,
        careTaskId: Long,
        taskTitle: String,
        reminderDateTime: Long
    ) {

        // Don't schedule reminders in the past
        if (reminderDateTime <= System.currentTimeMillis()) {
            return
        }

        val alarmManager = context.getSystemService(
            Context.ALARM_SERVICE
        ) as AlarmManager

        val intent = Intent(
            context,
            ReminderReceiver::class.java
        ).apply {
            putExtra("taskTitle", taskTitle)
        }

        val requestCode = getRequestCode(
            careTaskId,
            reminderDateTime
        )

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or
                    PendingIntent.FLAG_IMMUTABLE
        )

        // Schedule the notification
        alarmManager.setAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            reminderDateTime,
            pendingIntent
        )
    }

    fun cancelReminder(
        context: Context,
        careTaskId: Long,
        reminderDateTime: Long
    ) {

        val alarmManager = context.getSystemService(
            Context.ALARM_SERVICE
        ) as AlarmManager

        val intent = Intent(
            context,
            ReminderReceiver::class.java
        )

        val requestCode = getRequestCode(
            careTaskId,
            reminderDateTime
        )

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_NO_CREATE or
                    PendingIntent.FLAG_IMMUTABLE
        ) ?: return

        // Cancel the scheduled alarm
        alarmManager.cancel(pendingIntent)
        pendingIntent.cancel()
    }
}
