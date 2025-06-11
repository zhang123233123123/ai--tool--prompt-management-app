package com.example.aipromptmanager.service

import android.app.Service
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.IBinder
import android.view.Gravity
import android.view.WindowManager
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aipromptmanager.data.Prompt
import com.example.aipromptmanager.viewmodel.PromptViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class FloatingPromptService : Service(), ViewModelStoreOwner {

    private lateinit var windowManager: WindowManager
    private lateinit var floatingView: ComposeView
    private val viewModelStore = ViewModelStore()
    
    override val viewModelStore: ViewModelStore
        get() = viewModelStore

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        createFloatingView()
    }

    private fun createFloatingView() {
        floatingView = ComposeView(this).apply {
            setContent {
                FloatingBubbleContent()
            }
        }

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.START
            x = 0
            y = 100
        }

        windowManager.addView(floatingView, params)
    }

    @Composable
    private fun FloatingBubbleContent() {
        var isExpanded by remember { mutableStateOf(false) }
        var offsetX by remember { mutableStateOf(0f) }
        var offsetY by remember { mutableStateOf(0f) }

        val viewModel: PromptViewModel = viewModel()
        val prompts by viewModel.prompts.collectAsState()

        Box(
            modifier = Modifier
                .offset(offsetX.dp, offsetY.dp)
                .pointerInput(Unit) {
                    detectDragGestures { change, _ ->
                        offsetX += change.x
                        offsetY += change.y
                    }
                }
        ) {
            if (isExpanded) {
                ExpandedPromptList(
                    prompts = prompts,
                    onPromptSelected = { prompt ->
                        copyToClipboard(prompt)
                        isExpanded = false
                    },
                    onClose = { isExpanded = false }
                )
            } else {
                FloatingBubble(
                    onClick = { isExpanded = true }
                )
            }
        }
    }

    @Composable
    private fun FloatingBubble(onClick: () -> Unit) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary)
                .clickable { onClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.AutoAwesome,
                contentDescription = "AI助手",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }
    }

    @Composable
    private fun ExpandedPromptList(
        prompts: List<Prompt>,
        onPromptSelected: (Prompt) -> Unit,
        onClose: () -> Unit
    ) {
        Card(
            modifier = Modifier
                .width(300.dp)
                .heightIn(max = 400.dp),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "AI提示词",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onClose) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "关闭"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn {
                    items(prompts.take(10)) { prompt ->
                        PromptListItem(
                            prompt = prompt,
                            onSelected = { onPromptSelected(prompt) }
                        )
                    }
                }

                if (prompts.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "暂无提示词",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun PromptListItem(
        prompt: Prompt,
        onSelected: () -> Unit
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .clickable { onSelected() },
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = prompt.title,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    Chip(
                        onClick = { },
                        colors = ChipDefaults.chipColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        )
                    ) {
                        Text(
                            text = prompt.category,
                            fontSize = 10.sp
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = prompt.content,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }

    private fun copyToClipboard(prompt: Prompt) {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("AI Prompt", prompt.content)
        clipboard.setPrimaryClip(clip)
        
        Toast.makeText(this, "已复制: ${prompt.title}", Toast.LENGTH_SHORT).show()
        
        // 增加使用次数
        // viewModel.incrementUsageCount(prompt.id)
    }

    override fun onDestroy() {
        super.onDestroy()
        windowManager.removeView(floatingView)
        viewModelStore.clear()
    }
} 