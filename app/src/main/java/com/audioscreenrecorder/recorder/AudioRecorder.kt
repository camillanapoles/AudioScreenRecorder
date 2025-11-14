package com.audioscreenrecorder.recorder

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import java.io.File
import java.io.IOException

class AudioRecorder(private val context: Context, private val outputFile: File) {
    
    private var mediaRecorder: MediaRecorder? = null
    private var isRecording = false
    
    @Throws(IOException::class)
    fun start() {
        if (isRecording) return
        
        mediaRecorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(context)
        } else {
            @Suppress("DEPRECATION")
            MediaRecorder()
        }
        
        try {
            // Set audio source
            mediaRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
            
            // Set output format based on file extension
            val format = when (outputFile.extension.lowercase()) {
                "aac" -> MediaRecorder.OutputFormat.AAC_ADTS
                "mp3", "mp4" -> MediaRecorder.OutputFormat.MPEG_4
                "3gp" -> MediaRecorder.OutputFormat.THREE_GPP
                else -> MediaRecorder.OutputFormat.MPEG_4
            }
            mediaRecorder?.setOutputFormat(format)
            
            // Set audio encoder
            mediaRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            
            // Set output file
            mediaRecorder?.setOutputFile(outputFile.absolutePath)
            
            // Prepare and start
            mediaRecorder?.prepare()
            mediaRecorder?.start()
            
            isRecording = true
        } catch (e: Exception) {
            release()
            throw e
        }
    }
    
    fun stop() {
        if (!isRecording) return
        
        try {
            mediaRecorder?.stop()
        } catch (e: Exception) {
            // Ignore stop exception
        } finally {
            release()
            isRecording = false
        }
    }
    
    private fun release() {
        try {
            mediaRecorder?.release()
        } catch (e: Exception) {
            // Ignore release exception
        } finally {
            mediaRecorder = null
        }
    }
}
