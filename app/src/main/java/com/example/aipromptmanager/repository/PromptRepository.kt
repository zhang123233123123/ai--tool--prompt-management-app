package com.example.aipromptmanager.repository

import com.example.aipromptmanager.data.Prompt
import com.example.aipromptmanager.data.PromptDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PromptRepository @Inject constructor(
    private val promptDao: PromptDao
) {
    
    fun getAllPrompts(): Flow<List<Prompt>> = promptDao.getAllPrompts()
    
    fun getPromptsByCategory(category: String): Flow<List<Prompt>> = 
        promptDao.getPromptsByCategory(category)
    
    fun getFavoritePrompts(): Flow<List<Prompt>> = promptDao.getFavoritePrompts()
    
    fun searchPrompts(query: String): Flow<List<Prompt>> = promptDao.searchPrompts(query)
    
    suspend fun getPromptById(id: Long): Prompt? = promptDao.getPromptById(id)
    
    suspend fun insertPrompt(prompt: Prompt): Long = promptDao.insertPrompt(prompt)
    
    suspend fun updatePrompt(prompt: Prompt) = promptDao.updatePrompt(prompt)
    
    suspend fun deletePrompt(prompt: Prompt) = promptDao.deletePrompt(prompt)
    
    suspend fun incrementUsageCount(id: Long) = promptDao.incrementUsageCount(id)
    
    fun getAllCategories(): Flow<List<String>> = promptDao.getAllCategories()
} 