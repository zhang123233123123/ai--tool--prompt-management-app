package com.example.aipromptmanager

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class PromptManagerApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
    }
} 