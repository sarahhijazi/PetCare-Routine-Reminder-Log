
package com.example.petcare_routinereminderlog.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        Pet::class,
        CareTask::class,
        ActivityLog::class,
        Reminder::class
    ],
    version = 4,
    exportSchema = false
)
abstract class PetDatabase : RoomDatabase() {

    abstract fun petDao(): PetDao

    abstract fun careTaskDao(): CareTaskDao

    abstract fun activityLogDao(): ActivityLogDao

    abstract fun reminderDao(): ReminderDao

    companion object {

        @Volatile
        private var INSTANCE: PetDatabase? = null

        fun getDatabase(context: Context): PetDatabase {
            return INSTANCE ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PetDatabase::class.java,
                    "petcare_database"
                )
                    .addMigrations(
                        MIGRATION_1_2,
                        MIGRATION_2_3,
                        MIGRATION_3_4
                    )
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}
