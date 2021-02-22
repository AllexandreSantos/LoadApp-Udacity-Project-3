package com.allexandresantos.detail

import android.app.Application
import android.app.NotificationManager
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.allexandresantos.cancelNotifications

class DetailViewModel(val app: Application): AndroidViewModel(app) {

    init {
        cancelNotifications()
    }

    fun test(){
        Log.d("oi", "chamou")
    }

    private fun cancelNotifications() {
        val notificationManager =
            ContextCompat.getSystemService(
                app,
                NotificationManager::class.java
            ) as NotificationManager
        notificationManager.cancelNotifications()
    }
}