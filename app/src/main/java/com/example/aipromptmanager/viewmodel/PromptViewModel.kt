package com.example.aipromptmanager.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aipromptmanager.data.Prompt
import com.example.aipromptmanager.repository.PromptRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PromptViewModel @Inject constructor(
    private val repository: PromptRepository
) : ViewModel() {
    
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    
    private val _selectedCategory = MutableStateFlow("")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    val prompts: StateFlow<List<Prompt>> = combine(
        _searchQuery,
        _selectedCategory
    ) { query, category ->
        when {
            query.isNotBlank() -> repository.searchPrompts(query)
            category.isNotBlank() -> repository.getPromptsByCategory(category)
            else -> repository.getAllPrompts()
        }
    }.flatMapLatest { it }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    
    val categories: StateFlow<List<String>> = repository.getAllCategories()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }
    
    fun updateSelectedCategory(category: String) {
        _selectedCategory.value = category
    }
    
    fun addPrompt(title: String, content: String, category: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val prompt = Prompt(
                    title = title,
                    content = content,
                    category = category
                )
                repository.insertPrompt(prompt)
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun updatePrompt(prompt: Prompt) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.updatePrompt(prompt.copy(updatedAt = System.currentTimeMillis()))
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun deletePrompt(prompt: Prompt) {
        viewModelScope.launch {
            repository.deletePrompt(prompt)
        }
    }
    
    fun incrementUsageCount(promptId: Long) {
        viewModelScope.launch {
            repository.incrementUsageCount(promptId)
        }
    }
    
    fun toggleFavorite(prompt: Prompt) {
        viewModelScope.launch {
            repository.updatePrompt(
                prompt.copy(
                    isFavorite = !prompt.isFavorite,
                    updatedAt = System.currentTimeMillis()
                )
            )
        }
    }
} 