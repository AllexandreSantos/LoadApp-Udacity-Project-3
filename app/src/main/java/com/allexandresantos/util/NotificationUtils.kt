package com.allexandresantos

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.allexandresantos.detail.DetailActivity
import com.allexandresantos.main.MainActivity
import com.allexandresantos.util.DOWNLOAD_NAME
import com.allexandresantos.util.DOWNLOAD_STATUS

// Notification ID.
private val NOTIFICATION_ID = 0
private val REQUEST_CODE = 0
private val FLAGS = 0

fun NotificationManager.sendNotification(applicationContext: Context, downloadName: String, downloadStatus: String) {
    // Create the content intent for the notification, which launches
    // this activity
    val mainIntent = Intent(applicationContext, MainActivity::class.java)
    mainIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
    val mainPendingIntent = PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        mainIntent,
        PendingIntent.FLAG_CANCEL_CURRENT
    )

    val detailIntent = Intent(applicationContext, DetailActivity::class.java)
    detailIntent.putExtra(DOWNLOAD_NAME, downloadName)
    detailIntent.putExtra(DOWNLOAD_STATUS, downloadStatus)
    val detailPendingIntent = PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        detailIntent,
        PendingIntent.FLAG_CANCEL_CURRENT
    )

    // Build the notification
    val builder = NotificationCompat.Builder(
        applicationContext,
        applicationContext.getString(R.string.finished_download_channel_id)
    )
        .setContentTitle(applicationContext
        .getString(R.string.notification_title))
        .setContentText(applicationContext.getString(R.string.download_finished))
        .setContentIntent(mainPendingIntent)

        .addAction(R.drawable.cloud_download,
            applicationContext.getString(R.string.check_status),
            detailPendingIntent
        )

        .setSmallIcon(R.drawable.ic_assistant_black_24dp)
        .setAutoCancel(true)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
    notify(NOTIFICATION_ID, builder.build())
}

/**
 * Cancels all notifications.
 *
 */
fun NotificationManager.cancelNotifications() {
    cancelAll()
}