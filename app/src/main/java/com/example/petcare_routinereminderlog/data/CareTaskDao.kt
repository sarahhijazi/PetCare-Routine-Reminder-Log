
package com.example.petcare_routinereminderlog.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CareTaskDao {

    @Insert
    suspend fun insertTask(task: CareTask)

    @Update
    suspend fun updateTask(task: CareTask)

    @Delete
    suspend fun deleteTask(task: CareTask)

    @Query(
        "SELECT * FROM care_tasks WHERE petId = :petId ORDER BY id DESC"
    )
    fun getTasksForPet(petId: Long): Flow<List<CareTask>>
}


