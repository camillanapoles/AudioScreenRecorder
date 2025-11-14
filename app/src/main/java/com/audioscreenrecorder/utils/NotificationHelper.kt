package com.audioscreenrecorder.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.audioscreenrecorder.R
import com.audioscreenrecorder.ui.MainActivity

class NotificationHelper(private val context: Context) {
    
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    
    companion object {
        private const val CHANNEL_ID = "AudioScreenRecorder"
        private const val NOTIFICATION_ID = 2
    }
    
    init {
        createNotificationChannel()
    }
    
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "AudioScreenRecorder",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notifications for AudioScreenRecorder"
            }
            
            notificationManager.createNotificationChannel(channel)
        }
    }
    
    fun showRecordingNotification() {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_record)
            .setContentTitle("Gravando")
            .setContentText("A gravação está em andamento")
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .build()
        
        notificationManager.notify(NOTIFICATION_ID, notification)
    }
    
    fun hideRecordingNotification() {
        notificationManager.cancel(NOTIFICATION_ID)
    }
}
