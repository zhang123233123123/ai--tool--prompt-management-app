package com.example.aipromptmanager.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PromptDao {
    
    @Query("SELECT * FROM prompts ORDER BY updatedAt DESC")
    fun getAllPrompts(): Flow<List<Prompt>>
    
    @Query("SELECT * FROM prompts WHERE category = :category ORDER BY updatedAt DESC")
    fun getPromptsByCategory(category: String): Flow<List<Prompt>>
    
    @Query("SELECT * FROM prompts WHERE isFavorite = 1 ORDER BY updatedAt DESC")
    fun getFavoritePrompts(): Flow<List<Prompt>>
    
    @Query("SELECT * FROM prompts WHERE title LIKE '%' || :query || '%' OR content LIKE '%' || :query || '%' ORDER BY updatedAt DESC")
    fun searchPrompts(query: String): Flow<List<Prompt>>
    
    @Query("SELECT * FROM prompts WHERE id = :id")
    suspend fun getPromptById(id: Long): Prompt?
    
    @Insert
    suspend fun insertPrompt(prompt: Prompt): Long
    
    @Update
    suspend fun updatePrompt(prompt: Prompt)
    
    @Delete
    suspend fun deletePrompt(prompt: Prompt)
    
    @Query("UPDATE prompts SET usageCount = usageCount + 1 WHERE id = :id")
    suspend fun incrementUsageCount(id: Long)
    
    @Query("SELECT DISTINCT category FROM prompts")
    fun getAllCategories(): Flow<List<String>>
} 