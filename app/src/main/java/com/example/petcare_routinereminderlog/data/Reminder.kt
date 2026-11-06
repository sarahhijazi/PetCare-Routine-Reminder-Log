
package com.example.petcare_routinereminderlog.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "reminders",
    foreignKeys = [
        ForeignKey(
            entity = CareTask::class,
            parentColumns = ["id"],
            childColumns = ["careTaskId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["careTaskId"])]
)
data class Reminder(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val careTaskId: Long,

    val reminderDateTime: Long,

    val isActive: Boolean = true
)
