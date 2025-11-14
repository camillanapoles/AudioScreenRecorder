package com.audioscreenrecorder.utils

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class PermissionsHelper(private val activity: AppCompatActivity) {
    
    private var permissionCallback: ((Boolean) -> Unit)? = null
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<Array<String>>
    
    init {
        setupPermissionLauncher()
    }
    
    private fun setupPermissionLauncher() {
        requestPermissionLauncher = activity.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            val allGranted = permissions.values.all { it }
            permissionCallback?.invoke(allGranted)
        }
    }
    
    fun hasAllPermissions(): Boolean {
        val permissions = getRequiredPermissions()
        return permissions.all { permission ->
            ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED
        }
    }
    
    fun requestAllPermissions(callback: (Boolean) -> Unit) {
        permissionCallback = callback
        val permissions = getRequiredPermissions()
        val notGrantedPermissions = permissions.filter { permission ->
            ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED
        }
        
        if (notGrantedPermissions.isEmpty()) {
            callback(true)
            return
        }
        
        requestPermissionLauncher.launch(notGrantedPermissions.toTypedArray())
    }
    
    private fun getRequiredPermissions(): Array<String> {
        val permissions = mutableListOf<String>()
        
        // Always required
        permissions.add(Manifest.permission.RECORD_AUDIO)
        
        // For Android 13 and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions.add(Manifest.permission.POST_NOTIFICATIONS)
        }
        
        // For older Android versions
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2) {
            permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        
        return permissions.toTypedArray()
    }
}
