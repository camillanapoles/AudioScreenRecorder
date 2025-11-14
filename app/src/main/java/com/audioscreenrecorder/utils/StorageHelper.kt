package com.audioscreenrecorder.utils

import android.content.Context
import android.os.Build
import android.os.Environment
import java.io.File

class StorageHelper(private val context: Context) {
    
    fun getAppExternalFilesDir(folderName: String = "AudioScreenRecorder"): File {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            File(context.getExternalFilesDir(null), folderName)
        } else {
            @Suppress("DEPRECATION")
            File(Environment.getExternalStorageDirectory(), folderName)
        }
    }
    
    fun ensureDirectoryExists(directory: File): Boolean {
        return if (directory.exists()) {
            directory.isDirectory
        } else {
            directory.mkdirs()
        }
    }
    
    fun getAvailableStorageSpace(directory: File): Long {
        return try {
            directory.freeSpace
        } catch (e: Exception) {
            Long.MAX_VALUE
        }
    }
}
