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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import org.classapp.sleepwell.utils.computeDecibel

fun computeAverageDecibel(values: List<Double>): Double {
    return if (values.isNotEmpty()) values.average() else 0.0
}

@Composable
fun DecibelMeterSection(
    audioPermissionGranted: Boolean,
    recording: Boolean,
    onAverageDecibelComputed: (Double) -> Unit
) {
    var decibel by remember { mutableDoubleStateOf(0.0) }
    val decibelHistory = remember { mutableStateListOf<Double>() }

    // Start recording and calculating decibels
    LaunchedEffect(recording) {
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

                if (recording) {
                    // Reset decibel history when recording starts
                    decibelHistory.clear()
                    Log.d("DecibelMeter", "Starting new recording session.")
                }

                // Loop to record audio and store decibels
                while (recording) {
                    val read = audioRecord.read(buffer, 0, bufSize)
                    decibel = computeDecibel(buffer, read)
                    decibelHistory.add(decibel)
                    delay(1000) // Update decibel level every 1 second
                }

                // Once recording stops, stop the audio record
                audioRecord.stop()
                audioRecord.release()

                // Log the average decibel after recording stops
                val averageDecibel = computeAverageDecibel(decibelHistory)
                onAverageDecibelComputed(averageDecibel)

            } catch (e: SecurityException) {
                Log.e("DecibelMeterSection", "Error recording audio", e)
            }
        }
    }

    // Column to display current decibel during recording
    Column(modifier = Modifier.padding(16.dp)) {
        if (audioPermissionGranted) {
            Text("Microphone permission granted. Decibel meter ready.")
            Spacer(modifier = Modifier.height(8.dp))
            if (recording) {
                Text("Current decibel level: %.2f dB".format(decibel))
            } else {
                Text("Decibel meter is idle.")
            }
        } else {
            Text("Waiting for microphone permission...")
        }
    }
}