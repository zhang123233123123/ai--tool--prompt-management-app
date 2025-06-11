package com.example.aipromptmanager.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.aipromptmanager.data.Prompt
import com.example.aipromptmanager.data.PromptCategory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PromptDialog(
    prompt: Prompt? = null,
    onDismiss: () -> Unit,
    onSave: (title: String, content: String, category: String) -> Unit
) {
    var title by remember { mutableStateOf(prompt?.title ?: "") }
    var content by remember { mutableStateOf(prompt?.content ?: "") }
    var selectedCategory by remember { mutableStateOf(prompt?.category ?: PromptCategory.OTHER.name) }
    var expanded by remember { mutableStateOf(false) }
    
    var titleError by remember { mutableStateOf(false) }
    var contentError by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = if (prompt != null) "编辑提示词" else "添加提示词",
                    style = MaterialTheme.typography.titleLarge
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // 标题输入
                OutlinedTextField(
                    value = title,
                    onValueChange = { 
                        title = it
                        titleError = false
                    },
                    label = { Text("标题") },
                    isError = titleError,
                    supportingText = if (titleError) {
                        { Text("标题不能为空") }
                    } else null,
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // 分类选择
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = PromptCategory.values()
                            .find { it.name == selectedCategory }?.displayName ?: "其他",
                        onValueChange = { },
                        readOnly = true,
                        label = { Text("分类") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        PromptCategory.values().forEach { category ->
                            DropdownMenuItem(
                                text = { Text(category.displayName) },
                                onClick = {
                                    selectedCategory = category.name
                                    expanded = false
                                }
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // 内容输入
                OutlinedTextField(
                    value = content,
                    onValueChange = { 
                        content = it
                        contentError = false
                    },
                    label = { Text("提示词内容") },
                    isError = contentError,
                    supportingText = if (contentError) {
                        { Text("内容不能为空") }
                    } else null,
                    minLines = 5,
                    maxLines = 10,
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // 按钮
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("取消")
                    }
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    Button(
                        onClick = {
                            titleError = title.isBlank()
                            contentError = content.isBlank()
                            
                            if (!titleError && !contentError) {
                                onSave(title.trim(), content.trim(), selectedCategory)
                            }
                        }
                    ) {
                        Text("保存")
                    }
                }
            }
        }
    }
} 