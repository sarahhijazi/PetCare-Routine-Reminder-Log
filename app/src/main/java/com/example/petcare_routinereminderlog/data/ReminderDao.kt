
package com.example.petcare_routinereminderlog.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ReminderDao {

    @Insert
    suspend fun insertReminder(reminder: Reminder)

    @Update
    suspend fun updateReminder(reminder: Reminder)

    @Delete
    suspend fun deleteReminder(reminder: Reminder)

    @Query("SELECT * FROM reminders WHERE careTaskId = :careTaskId ORDER BY reminderDateTime ASC")
    fun getRemindersForTask(careTaskId: Long): Flow<List<Reminder>>
}
