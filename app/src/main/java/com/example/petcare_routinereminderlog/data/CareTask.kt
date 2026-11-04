
package com.example.petcare_routinereminderlog.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "care_tasks",
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
data class CareTask(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val petId: Long,

    val title: String,

    val category: String,

    val frequency: String,

    val isCompleted: Boolean = false
)


