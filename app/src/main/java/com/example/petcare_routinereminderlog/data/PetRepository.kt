
package com.example.petcare_routinereminderlog.data

import androidx.room.withTransaction
import kotlinx.coroutines.flow.Flow

class PetRepository(
    private val database: PetDatabase,
    private val petDao: PetDao,
    private val careTaskDao: CareTaskDao,
    private val activityLogDao: ActivityLogDao,
    private val reminderDao: ReminderDao
) {

    val allPets: Flow<List<Pet>> = petDao.getAllPets()

    suspend fun insertPet(pet: Pet) {
        petDao.insertPet(pet)
    }

    suspend fun deletePet(pet: Pet) {
        petDao.deletePet(pet)
    }

    fun getTasksForPet(petId: Long): Flow<List<CareTask>> {
        return careTaskDao.getTasksForPet(petId)
    }

    suspend fun insertTask(task: CareTask) {
        careTaskDao.insertTask(task)
    }

    suspend fun updateTask(task: CareTask) {
        careTaskDao.updateTask(task)
    }

    suspend fun deleteTask(task: CareTask) {
        careTaskDao.deleteTask(task)
    }

    suspend fun insertActivityLog(log: ActivityLog) {
        activityLogDao.insertLog(log)
    }

    fun getLogsForPet(petId: Long): Flow<List<ActivityLog>> {
        return activityLogDao.getLogsForPet(petId)
    }

    suspend fun completeTask(task: CareTask) {
        database.withTransaction {
            if (!task.isCompleted) {
                careTaskDao.updateTask(
                    task.copy(isCompleted = true)
                )

                activityLogDao.insertLog(
                    ActivityLog(
                        petId = task.petId,
                        taskTitle = task.title,
                        category = task.category,
                        completedAt = System.currentTimeMillis()
                    )
                )
            } else {
                careTaskDao.updateTask(
                    task.copy(isCompleted = false)
                )
            }
        }
    }

    // REMINDER FUNCTIONS

    suspend fun insertReminder(reminder: Reminder) {
        reminderDao.insertReminder(reminder)
    }

    suspend fun updateReminder(reminder: Reminder) {
        reminderDao.updateReminder(reminder)
    }

    suspend fun deleteReminder(reminder: Reminder) {
        reminderDao.deleteReminder(reminder)
    }

    fun getRemindersForTask(careTaskId: Long): Flow<List<Reminder>> {
        return reminderDao.getRemindersForTask(careTaskId)
    }
}
