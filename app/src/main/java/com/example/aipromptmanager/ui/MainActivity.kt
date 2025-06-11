package com.example.aipromptmanager.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.aipromptmanager.service.FloatingPromptService
import com.example.aipromptmanager.ui.theme.AIPromptManagerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    private val overlayPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        // 权限请求结果处理
        if (Settings.canDrawOverlays(this)) {
            startFloatingService()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            AIPromptManagerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(
                        onStartFloatingService = {
                            if (Settings.canDrawOverlays(this@MainActivity)) {
                                startFloatingService()
                            } else {
                                requestOverlayPermission()
                            }
                        },
                        onStopFloatingService = {
                            stopFloatingService()
                        }
                    )
                }
            }
        }
    }

    private fun requestOverlayPermission() {
        val intent = Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:$packageName")
        )
        overlayPermissionLauncher.launch(intent)
    }

    private fun startFloatingService() {
        val intent = Intent(this, FloatingPromptService::class.java)
        startService(intent)
    }

    private fun stopFloatingService() {
        val intent = Intent(this, FloatingPromptService::class.java)
        stopService(intent)
    }
} 