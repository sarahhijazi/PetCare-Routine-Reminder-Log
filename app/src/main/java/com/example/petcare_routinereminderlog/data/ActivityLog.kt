
package com.example.petcare_routinereminderlog.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "activity_logs",
    foreignKeys = [
        ForeignKey(
            entity = Pet::class,
            parentColumns = ["id"],
            childColumns = ["petId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["petId"])]
)
data class ActivityLog(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val petId: Long,

    val taskTitle: String,

    val category: String,

    val completedAt: Long = System.currentTimeMillis()
)


