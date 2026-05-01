package com.ndejje.votingapp.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// 1. Updated entities list and version
@Database(
    entities = [UserEntity::class, CandidateEntity::class],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    // 2. Added the new DAO
    abstract fun userDao(): UserDao
    abstract fun candidateDao(): CandidateDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "ndejje_voting_db"
                )
                    // 3. Added fallback to destructive migration to handle version changes
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}