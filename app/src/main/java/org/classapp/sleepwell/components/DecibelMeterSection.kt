package org.classapp.sleepwell.components

import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.math.log10
import kotlin.math.sqrt

@Composable
fun DecibelMeterSection(audioPermissionGranted: Boolean) {
    var decibel by remember { mutableDoubleStateOf(0.0) }

    // TODO run in background + fix zero decibel bug
    LaunchedEffect(audioPermissionGranted) {
        if (audioPermissionGranted) {
            val sampleRate = 44100

            val bufSize = AudioRecord.getMinBufferSize(
                sampleRate,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT
            )

            try {
                val audioRecord = AudioRecord(
                    MediaRecorder.AudioSource.MIC, sampleRate,
                    AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, bufSize
                )
                val buffer = ShortArray(bufSize)
                audioRecord.startRecording()

                while (true) {
                    val read = audioRecord.read(buffer, 0, bufSize)
                    if (read > 0) {
                        val sumSquares = buffer.take(read).sumOf { (it * it).toDouble() }
                        val rms = sqrt(sumSquares / read)
                        val epsilon = 1e-6
                        decibel = if (rms > epsilon) 20 * log10(rms) else 0.0
                        Log.d("DecibelMeter", "RMS: $rms, dB: $decibel") // Debug Print
                    }
                    delay(1000) // 1 update/sec
                }
            } catch (e: SecurityException) {
                Log.e("DecibelMeterSection", "Error recording audio")
            }
        }
    }

    // Displaying decibel level or waiting for permission
    Column(modifier = Modifier.padding(16.dp)) {
        if (audioPermissionGranted) {
            Text("Microphone permission granted. Decibel meter active.")
            Spacer(modifier = Modifier.height(8.dp))
            Text("Current decibel level: %.2f dB".format(decibel))
        } else {
            Text("Waiting for microphone permission...")
        }
    }
}
