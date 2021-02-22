package com.allexandresantos.receiver

import android.app.ActivityManager
import android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
import android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE
import android.app.DownloadManager
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat
import com.allexandresantos.R
import com.allexandresantos.main.MainActivity
import com.allexandresantos.sendNotification

class DownloadReceiver: BroadcastReceiver() {


    override fun onReceive(context: Context, intent: Intent?) {


//        fun foregrounded(): Boolean {
//            val appProcessInfo = ActivityManager.RunningAppProcessInfo();
//            ActivityManager.getMyMemoryState(appProcessInfo);
//            return (appProcessInfo.importance == IMPORTANCE_FOREGROUND || appProcessInfo.importance == IMPORTANCE_VISIBLE)
//        }




        val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
        Log.d("oi", "onReceive: ID " + id)


        val notificationManager = ContextCompat.getSystemService(
            context,
            NotificationManager::class.java
        ) as NotificationManager

        notificationManager.sendNotification(
            context.getText(R.string.download_finished).toString(),
            context
        )

    }

}