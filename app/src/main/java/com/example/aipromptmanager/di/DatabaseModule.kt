package com.example.aipromptmanager.di

import android.content.Context
import androidx.room.Room
import com.example.aipromptmanager.data.PromptDao
import com.example.aipromptmanager.data.PromptDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun providePromptDatabase(@ApplicationContext context: Context): PromptDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            PromptDatabase::class.java,
            "prompt_database"
        ).build()
    }

    @Provides
    fun providePromptDao(database: PromptDatabase): PromptDao {
        return database.promptDao()
    }
} 