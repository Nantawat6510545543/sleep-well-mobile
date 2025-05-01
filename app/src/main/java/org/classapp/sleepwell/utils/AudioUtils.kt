package org.classapp.sleepwell.utils

import android.media.MediaRecorder
import java.io.IOException
import kotlin.math.log10

class AudioDecibelMeter {
    private var mediaRecorder: MediaRecorder? = null

    fun start() {
        mediaRecorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.DEFAULT)
            setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT)
            setOutputFile("/dev/null") // discard audio
            try {
                prepare()
                start()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun getDecibels(): Int {
        return mediaRecorder?.maxAmplitude?.let {
            if (it > 0) (20 * log10(it.toDouble())).toInt() else 0
        } ?: 0
    }

    fun stop() {
        mediaRecorder?.apply {
            stop()
            release()
        }
        mediaRecorder = null
    }
}
