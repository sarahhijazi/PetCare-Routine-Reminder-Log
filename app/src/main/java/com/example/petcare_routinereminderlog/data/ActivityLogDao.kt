
package com.example.petcare_routinereminderlog.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ActivityLogDao {

    @Insert
    suspend fun insertLog(log: ActivityLog)

    @Query(
        "SELECT * FROM activity_logs " +
                "WHERE petId = :petId " +
                "ORDER BY completedAt DESC, id DESC"
    )
    fun getLogsForPet(
        petId: Long
    ): Flow<List<ActivityLog>>
}

