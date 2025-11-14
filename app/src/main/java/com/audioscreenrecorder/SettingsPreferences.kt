package com.audioscreenrecorder

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

class SettingsPreferences(context: Context) {
    private val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    
    companion object {
        private const val COUNTDOWN_TIME_KEY = "countdown_time"
        private const val SAVE_FOLDER_KEY = "save_folder"
        private const val AUDIO_FORMAT_KEY = "audio_format"
        private const val RECORD_MODE_KEY = "record_mode"
        
        // Default values
        private const val DEFAULT_COUNTDOWN_TIME = 3
        private const val DEFAULT_SAVE_FOLDER = "AudioScreenRecorder"
        private const val DEFAULT_AUDIO_FORMAT = "mp4"
        private const val DEFAULT_RECORD_MODE = "audio_only"
    }
    
    var countdownTime: Int
        get() = prefs.getString(COUNTDOWN_TIME_KEY, DEFAULT_COUNTDOWN_TIME.toString())?.toIntOrNull() ?: DEFAULT_COUNTDOWN_TIME
        set(value) = prefs.edit().putString(COUNTDOWN_TIME_KEY, value.toString()).apply()
    
    var saveFolder: String
        get() = prefs.getString(SAVE_FOLDER_KEY, DEFAULT_SAVE_FOLDER) ?: DEFAULT_SAVE_FOLDER
        set(value) = prefs.edit().putString(SAVE_FOLDER_KEY, value).apply()
    
    var audioFormat: String
        get() = prefs.getString(AUDIO_FORMAT_KEY, DEFAULT_AUDIO_FORMAT) ?: DEFAULT_AUDIO_FORMAT
        set(value) = prefs.edit().putString(AUDIO_FORMAT_KEY, value).apply()
    
    var recordMode: String
        get() = prefs.getString(RECORD_MODE_KEY, DEFAULT_RECORD_MODE) ?: DEFAULT_RECORD_MODE
        set(value) = prefs.edit().putString(RECORD_MODE_KEY, value).apply()
}
