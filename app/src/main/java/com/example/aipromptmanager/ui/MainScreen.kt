package com.example.aipromptmanager.ui

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.aipromptmanager.data.Prompt
import com.example.aipromptmanager.data.PromptCategory
import com.example.aipromptmanager.viewmodel.PromptViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onStartFloatingService: () -> Unit,
    onStopFloatingService: () -> Unit,
    viewModel: PromptViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    var showAddDialog by remember { mutableStateOf(false) }
    var editingPrompt by remember { mutableStateOf<Prompt?>(null) }
    
    val prompts by viewModel.prompts.collectAsState()
    val categories by viewModel.categories.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    
    val hasOverlayPermission = Settings.canDrawOverlays(context)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 权限和服务控制区域
        PermissionCard(
            hasOverlayPermission = hasOverlayPermission,
            onStartFloatingService = onStartFloatingService,
            onStopFloatingService = onStopFloatingService,
            onRequestAccessibilityPermission = {
                val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                context.startActivity(intent)
            }
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // 搜索栏
        OutlinedTextField(
            value = searchQuery,
            onValueChange = viewModel::updateSearchQuery,
            label = { Text("搜索提示词") },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = "搜索")
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { viewModel.updateSearchQuery("") }) {
                        Icon(Icons.Default.Clear, contentDescription = "清除")
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // 分类过滤
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            item {
                FilterChip(
                    onClick = { viewModel.updateSelectedCategory("") },
                    label = { Text("全部") },
                    selected = selectedCategory.isEmpty()
                )
            }
            items(PromptCategory.values()) { category ->
                FilterChip(
                    onClick = { viewModel.updateSelectedCategory(category.name) },
                    label = { Text(category.displayName) },
                    selected = selectedCategory == category.name
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // 添加按钮和提示词列表
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "提示词列表",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            
            FloatingActionButton(
                onClick = { showAddDialog = true },
                modifier = Modifier.size(48.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "添加提示词")
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // 提示词列表
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(prompts) { prompt ->
                PromptCard(
                    prompt = prompt,
                    onEdit = { editingPrompt = it },
                    onDelete = { viewModel.deletePrompt(it) },
                    onToggleFavorite = { viewModel.toggleFavorite(it) },
                    onCopy = { viewModel.incrementUsageCount(it.id) }
                )
            }
        }
    }
    
    // 添加/编辑对话框
    if (showAddDialog || editingPrompt != null) {
        PromptDialog(
            prompt = editingPrompt,
            onDismiss = {
                showAddDialog = false
                editingPrompt = null
            },
            onSave = { title, content, category ->
                if (editingPrompt != null) {
                    viewModel.updatePrompt(
                        editingPrompt!!.copy(
                            title = title,
                            content = content,
                            category = category
                        )
                    )
                } else {
                    viewModel.addPrompt(title, content, category)
                }
                showAddDialog = false
                editingPrompt = null
            }
        )
    }
}

@Composable
private fun PermissionCard(
    hasOverlayPermission: Boolean,
    onStartFloatingService: () -> Unit,
    onStopFloatingService: () -> Unit,
    onRequestAccessibilityPermission: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "服务控制",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (hasOverlayPermission) "悬浮窗权限已授予" else "需要悬浮窗权限",
                    color = if (hasOverlayPermission) 
                        MaterialTheme.colorScheme.primary 
                    else 
                        MaterialTheme.colorScheme.error
                )
                
                if (hasOverlayPermission) {
                    Row {
                        Button(
                            onClick = onStartFloatingService,
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            Text("启动悬浮助手")
                        }
                        
                        OutlinedButton(onClick = onStopFloatingService) {
                            Text("停止")
                        }
                    }
                } else {
                    Button(onClick = onStartFloatingService) {
                        Text("申请权限")
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("无障碍服务(可选)")
                
                OutlinedButton(onClick = onRequestAccessibilityPermission) {
                    Text("设置")
                }
            }
        }
    }
}

@Composable
private fun LazyRow(
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    modifier: Modifier = Modifier,
    content: androidx.compose.foundation.lazy.LazyListScope.() -> Unit
) {
    androidx.compose.foundation.lazy.LazyRow(
        horizontalArrangement = horizontalArrangement,
        modifier = modifier,
        content = content
    )
} 