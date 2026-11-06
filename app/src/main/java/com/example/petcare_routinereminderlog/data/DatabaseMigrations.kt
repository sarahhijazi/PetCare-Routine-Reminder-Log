
package com.example.petcare_routinereminderlog.data

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {

    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `care_tasks` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `petId` INTEGER NOT NULL,
                `title` TEXT NOT NULL,
                `category` TEXT NOT NULL,
                `frequency` TEXT NOT NULL,
                `isCompleted` INTEGER NOT NULL,
                FOREIGN KEY(`petId`)
                    REFERENCES `pets`(`id`)
                    ON UPDATE NO ACTION
                    ON DELETE CASCADE
            )
            """.trimIndent()
        )

        db.execSQL(
            """
            CREATE INDEX IF NOT EXISTS
            `index_care_tasks_petId`
            ON `care_tasks` (`petId`)
            """.trimIndent()
        )
    }
}

val MIGRATION_2_3 = object : Migration(2, 3) {

    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `activity_logs` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `petId` INTEGER NOT NULL,
                `taskTitle` TEXT NOT NULL,
                `category` TEXT NOT NULL,
                `completedAt` INTEGER NOT NULL,
                FOREIGN KEY(`petId`)
                    REFERENCES `pets`(`id`)
                    ON UPDATE NO ACTION
                    ON DELETE CASCADE
            )
            """.trimIndent()
        )

        db.execSQL(
            """
            CREATE INDEX IF NOT EXISTS
            `index_activity_logs_petId`
            ON `activity_logs` (`petId`)
            """.trimIndent()
        )
    }
}

val MIGRATION_3_4 = object : Migration(3, 4) {

    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `reminders` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `careTaskId` INTEGER NOT NULL,
                `reminderDateTime` INTEGER NOT NULL,
                `isActive` INTEGER NOT NULL,
                FOREIGN KEY(`careTaskId`)
                    REFERENCES `care_tasks`(`id`)
                    ON UPDATE NO ACTION
                    ON DELETE CASCADE
            )
            """.trimIndent()
        )

        db.execSQL(
            """
            CREATE INDEX IF NOT EXISTS
            `index_reminders_careTaskId`
            ON `reminders` (`careTaskId`)
            """.trimIndent()
        )
    }
}
