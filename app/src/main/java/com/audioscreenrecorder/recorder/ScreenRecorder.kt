package com.audioscreenrecorder.recorder

import android.content.Context
import android.hardware.display.VirtualDisplay
import android.media.MediaRecorder
import android.media.projection.MediaProjection
import android.os.Build
import android.util.DisplayMetrics
import java.io.File
import java.io.IOException

class ScreenRecorder(
    private val context: Context,
    private val mediaProjection: MediaProjection,
    private val displayMetrics: DisplayMetrics,
    private val outputFile: File
) {
    
    private var mediaRecorder: MediaRecorder? = null
    private var virtualDisplay: VirtualDisplay? = null
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
            // Set video source
            mediaRecorder?.setVideoSource(MediaRecorder.VideoSource.SURFACE)
            
            // Set audio source
            mediaRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
            
            // Set output format
            mediaRecorder?.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            
            // Set video encoder
            mediaRecorder?.setVideoEncoder(MediaRecorder.VideoEncoder.H264)
            
            // Set audio encoder
            mediaRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            
            // Set video size and bit rate
            mediaRecorder?.setVideoSize(displayMetrics.widthPixels, displayMetrics.heightPixels)
            mediaRecorder?.setVideoEncodingBitRate(8 * 1000 * 1000)
            mediaRecorder?.setVideoFrameRate(30)
            
            // Set audio encoding bit rate and sample rate
            mediaRecorder?.setAudioEncodingBitRate(128000)
            mediaRecorder?.setAudioSamplingRate(44100)
            
            // Set output file
            mediaRecorder?.setOutputFile(outputFile.absolutePath)
            
            // Prepare and start
            mediaRecorder?.prepare()
            
            val surface = mediaRecorder?.surface
            virtualDisplay = mediaProjection.createVirtualDisplay(
                "ScreenRecorder",
                displayMetrics.widthPixels,
                displayMetrics.heightPixels,
                displayMetrics.densityDpi,
                0,
                surface,
                null,
                null
            )
            
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
            virtualDisplay?.release()
        } catch (e: Exception) {
            // Ignore release exception
        }
        
        try {
            mediaRecorder?.release()
        } catch (e: Exception) {
            // Ignore release exception
        } finally {
            virtualDisplay = null
            mediaRecorder = null
        }
    }
}
