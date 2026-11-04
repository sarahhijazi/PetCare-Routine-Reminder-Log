
package com.example.petcare_routinereminderlog.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PetDao {

    @Insert
    suspend fun insertPet(pet: Pet)

    @Query("SELECT * FROM pets ORDER BY id DESC")
    fun getAllPets(): Flow<List<Pet>>

    @Delete
    suspend fun deletePet(pet: Pet)
}

