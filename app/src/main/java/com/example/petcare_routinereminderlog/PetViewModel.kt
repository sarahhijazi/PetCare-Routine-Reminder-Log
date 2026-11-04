
package com.example.petcare_routinereminderlog

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.petcare_routinereminderlog.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class PetViewModel(application: Application) :
    AndroidViewModel(application) {

    private val database =
        PetDatabase.getDatabase(application)


    private val repository = PetRepository(
        database,
        database.petDao(),
        database.careTaskDao(),
        database.activityLogDao()
    )


    val allPets = repository.allPets.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun addPet(
        name: String,
        type: String,
        age: Int
    ) {
        viewModelScope.launch {
            repository.insertPet(
                Pet(
                    name = name,
                    type = type,
                    age = age
                )
            )
        }
    }

    fun deletePet(pet: Pet) {
        viewModelScope.launch {
            repository.deletePet(pet)
        }
    }

    fun getTasksForPet(
        petId: Long
    ): Flow<List<CareTask>> {
        return repository.getTasksForPet(petId)
    }

    fun addTask(
        petId: Long,
        title: String,
        category: String,
        frequency: String
    ) {
        viewModelScope.launch {
            repository.insertTask(
                CareTask(
                    petId = petId,
                    title = title,
                    category = category,
                    frequency = frequency
                )
            )
        }
    }

    fun updateTask(task: CareTask) {
        viewModelScope.launch {
            repository.updateTask(task)
        }
    }

    fun deleteTask(task: CareTask) {
        viewModelScope.launch {
            repository.deleteTask(task)
        }
    }


    fun completeTask(task: CareTask) {
        viewModelScope.launch {
            repository.completeTask(task)
        }
    }


    fun getActivityHistory(
        petId: Long
    ): Flow<List<ActivityLog>> {
        return repository.getLogsForPet(petId)
    }

}
