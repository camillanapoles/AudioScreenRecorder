package com.audioscreenrecorder.recorder

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.display.VirtualDisplay
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.os.Environment
import android.os.IBinder
import android.util.DisplayMetrics
import android.view.WindowManager
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.audioscreenrecorder.R
import com.audioscreenrecorder.ui.MainActivity
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MediaProjectionService : Service() {
    
    companion object {
        const val ACTION_START = "com.audioscreenrecorder.ACTION_START"
        const val ACTION_STOP = "com.audioscreenrecorder.ACTION_STOP"
        
        const val EXTRA_RESULT_CODE = "extra_result_code"
        const val EXTRA_RESULT_DATA = "extra_result_data"
        const val EXTRA_RECORD_MODE = "extra_record_mode"
        const val EXTRA_SAVE_FOLDER = "extra_save_folder"
        const val EXTRA_AUDIO_FORMAT = "extra_audio_format"
        
        private const val NOTIFICATION_CHANNEL_ID = "AudioScreenRecorder"
        private const val NOTIFICATION_ID = 1
    }
    
    private var mediaProjection: MediaProjection? = null
    private var virtualDisplay: VirtualDisplay? = null
    private var audioRecorder: AudioRecorder? = null
    private var screenRecorder: ScreenRecorder? = null
    
    private var recordMode = "audio_only"
    private var saveFolder = "AudioScreenRecorder"
    private var audioFormat = "mp4"
    
    private var isRecording = false
    private var recordingFile: File? = null
    
    override fun onBind(intent: Intent): IBinder? = null
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> {
                val resultCode = intent.getIntExtra(EXTRA_RESULT_CODE, -1)
                val resultData = intent.getParcelableExtra<Intent>(EXTRA_RESULT_DATA)
                recordMode = intent.getStringExtra(EXTRA_RECORD_MODE) ?: "audio_only"
                saveFolder = intent.getStringExtra(EXTRA_SAVE_FOLDER) ?: "AudioScreenRecorder"
                audioFormat = intent.getStringExtra(EXTRA_AUDIO_FORMAT) ?: "mp4"
                
                startRecording(resultCode, resultData)
            }
            ACTION_STOP -> {
                stopRecording()
            }
        }
        
        return START_NOT_STICKY
    }
    
    private fun startRecording(resultCode: Int, resultData: Intent?) {
        if (isRecording) return
        
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, createNotification())
        
        try {
            val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val fileName = "Recording_$timestamp"
            
            // Create output directory if needed
            val outputDir = File(getExternalFilesDir(null), saveFolder)
            if (!outputDir.exists()) {
                outputDir.mkdirs()
            }
            
            when (recordMode) {
                "audio_only" -> {
                    // Record only audio
                    recordingFile = File(outputDir, "${fileName}_audio.$audioFormat")
                    audioRecorder = AudioRecorder(this, recordingFile!!)
                    audioRecorder?.start()
                }
                "audio_screen" -> {
                    // Record both audio and screen
                    if (resultData != null) {
                        val mediaProjectionManager = getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
                        mediaProjection = mediaProjectionManager.getMediaProjection(resultCode, resultData)
                        
                        recordingFile = File(outputDir, "${fileName}_screen.mp4")
                        screenRecorder = ScreenRecorder(
                            this,
                            mediaProjection!!,
                            getDisplayMetrics(),
                            recordingFile!!
                        )
                        screenRecorder?.start()
                    }
                }
            }
            
            isRecording = true
            
            Toast.makeText(this, "Gravação iniciada", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Erro ao iniciar gravação: ${e.message}", Toast.LENGTH_LONG).show()
            stopSelf()
        }
    }
    
    private fun stopRecording() {
        if (!isRecording) return
        
        try {
            when (recordMode) {
                "audio_only" -> {
                    audioRecorder?.stop()
                    audioRecorder = null
                }
                "audio_screen" -> {
                    screenRecorder?.stop()
                    screenRecorder = null
                }
            }
            
            mediaProjection?.stop()
            virtualDisplay?.release()
            
            Toast.makeText(this, "Gravação salva em ${recordingFile?.absolutePath}", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Erro ao parar gravação: ${e.message}", Toast.LENGTH_LONG).show()
        } finally {
            isRecording = false
            stopForeground(STOP_FOREGROUND_REMOVE)
            stopSelf()
        }
    }
    
    private fun getDisplayMetrics(): DisplayMetrics {
        val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val displayMetrics = DisplayMetrics()
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            display?.getRealMetrics(displayMetrics)
        } else {
            @Suppress("DEPRECATION")
            windowManager.defaultDisplay.getMetrics(displayMetrics)
        }
        
        return displayMetrics
    }
    
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "AudioScreenRecorder Service",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
    }
    
    private fun createNotification(): Notification {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 
            0, 
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
        
        return NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle("AudioScreenRecorder")
            .setContentText("Gravando...")
            .setSmallIcon(R.drawable.ic_record)
            .setContentIntent(pendingIntent)
            .build()
    }
    
    override fun onDestroy() {
        super.onDestroy()
        
        if (isRecording) {
            stopRecording()
        }
    }
}
