package com.example.aipromptmanager.data

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context

@Database(
    entities = [Prompt::class],
    version = 1,
    exportSchema = false
)
abstract class PromptDatabase : RoomDatabase() {
    abstract fun promptDao(): PromptDao
    
    companion object {
        @Volatile
        private var INSTANCE: PromptDatabase? = null
        
        fun getDatabase(context: Context): PromptDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PromptDatabase::class.java,
                    "prompt_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
} 