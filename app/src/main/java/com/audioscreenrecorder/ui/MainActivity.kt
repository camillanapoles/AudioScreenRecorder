package com.audioscreenrecorder.ui

import android.content.Intent
import android.media.projection.MediaProjectionManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.audioscreenrecorder.R
import com.audioscreenrecorder.SettingsPreferences
import com.audioscreenrecorder.recorder.MediaProjectionService
import com.audioscreenrecorder.utils.NotificationHelper
import com.audioscreenrecorder.utils.PermissionsHelper

class MainActivity : AppCompatActivity() {
    
    private lateinit var recordButton: Button
    private lateinit var stopButton: Button
    private lateinit var settingsButton: Button
    private lateinit var statusText: TextView
    private lateinit var countdownText: TextView
    
    private lateinit var settingsPreferences: SettingsPreferences
    private lateinit var permissionsHelper: PermissionsHelper
    private lateinit var notificationHelper: NotificationHelper
    
    private var isRecording = false
    private var countdownHandler: Handler? = null
    private var countdownRunnable: Runnable? = null
    
    private val mediaProjectionLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            startRecordingService(result.resultCode, result.data)
        } else {
            Toast.makeText(this, "Permissão de gravação negada", Toast.LENGTH_SHORT).show()
            resetUI()
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        initViews()
        initHelpers()
        setupClickListeners()
        checkPermissions()
    }
    
    private fun initViews() {
        recordButton = findViewById(R.id.btn_record)
        stopButton = findViewById(R.id.btn_stop)
        settingsButton = findViewById(R.id.btn_settings)
        statusText = findViewById(R.id.tv_status)
        countdownText = findViewById(R.id.tv_countdown)
        
        settingsPreferences = SettingsPreferences(this)
    }
    
    private fun initHelpers() {
        permissionsHelper = PermissionsHelper(this)
        notificationHelper = NotificationHelper(this)
    }
    
    private fun setupClickListeners() {
        recordButton.setOnClickListener {
            if (!isRecording) {
                startCountdown()
            }
        }
        
        stopButton.setOnClickListener {
            if (isRecording) {
                stopRecording()
            }
        }
        
        settingsButton.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
    }
    
    private fun checkPermissions() {
        if (!permissionsHelper.hasAllPermissions()) {
            permissionsHelper.requestAllPermissions { granted ->
                if (!granted) {
                    Toast.makeText(this, "Permissões necessárias não concedidas", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
    
    private fun startCountdown() {
        recordButton.isEnabled = false
        countdownText.visibility = View.VISIBLE
        
        val countdownTime = settingsPreferences.countdownTime
        var counter = countdownTime
        
        countdownHandler = Handler(Looper.getMainLooper())
        countdownRunnable = object : Runnable {
            override fun run() {
                if (counter > 0) {
                    countdownText.text = counter.toString()
                    counter--
                    countdownHandler?.postDelayed(this, 1000)
                } else {
                    countdownText.visibility = View.GONE
                    requestMediaProjection()
                }
            }
        }
        
        countdownHandler?.post(countdownRunnable!!)
    }
    
    private fun requestMediaProjection() {
        val recordMode = settingsPreferences.recordMode
        
        if (recordMode == "audio_screen") {
            // Need MediaProjection permission for screen recording
            val mediaProjectionManager = getSystemService(MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
            mediaProjectionLauncher.launch(mediaProjectionManager.createScreenCaptureIntent())
        } else {
            // Audio only - no MediaProjection needed
            startRecordingService(RESULT_OK, null)
        }
    }
    
    private fun startRecordingService(resultCode: Int, data: Intent?) {
        val intent = Intent(this, MediaProjectionService::class.java).apply {
            action = MediaProjectionService.ACTION_START
            putExtra(MediaProjectionService.EXTRA_RESULT_CODE, resultCode)
            putExtra(MediaProjectionService.EXTRA_RESULT_DATA, data)
            putExtra(MediaProjectionService.EXTRA_RECORD_MODE, settingsPreferences.recordMode)
            putExtra(MediaProjectionService.EXTRA_SAVE_FOLDER, settingsPreferences.saveFolder)
            putExtra(MediaProjectionService.EXTRA_AUDIO_FORMAT, settingsPreferences.audioFormat)
        }
        
        startForegroundService(intent)
        
        isRecording = true
        updateUIForRecordingState()
    }
    
    private fun stopRecording() {
        val intent = Intent(this, MediaProjectionService::class.java).apply {
            action = MediaProjectionService.ACTION_STOP
        }
        
        startService(intent)
        
        isRecording = false
        resetUI()
    }
    
    private fun updateUIForRecordingState() {
        recordButton.visibility = View.GONE
        stopButton.visibility = View.VISIBLE
        statusText.text = "Gravando..."
        notificationHelper.showRecordingNotification()
    }
    
    private fun resetUI() {
        recordButton.visibility = View.VISIBLE
        recordButton.isEnabled = true
        stopButton.visibility = View.GONE
        statusText.text = "Pronto para gravar"
        countdownText.visibility = View.GONE
        notificationHelper.hideRecordingNotification()
    }
    
    override fun onDestroy() {
        super.onDestroy()
        countdownHandler?.removeCallbacks(countdownRunnable!!)
        
        if (isRecording) {
            stopRecording()
        }
    }
}
