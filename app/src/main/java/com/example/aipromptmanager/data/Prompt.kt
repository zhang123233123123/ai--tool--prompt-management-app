package com.example.aipromptmanager.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "prompts")
data class Prompt(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val content: String,
    val category: String,
    val isFavorite: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val usageCount: Int = 0
)

enum class PromptCategory(val displayName: String) {
    WRITING("写作"),
    CODING("编程"),
    TRANSLATION("翻译"),
    ANALYSIS("分析"),
    CREATIVE("创意"),
    OTHER("其他")
} 