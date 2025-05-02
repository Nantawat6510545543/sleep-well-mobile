package org.classapp.sleepwell.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun DecibelMeterSection(audioPermissionGranted: Boolean) {
    if (audioPermissionGranted) {
        Text("Microphone permission granted. Decibel meter would be active.")
        // Initialize decibel measuring logic here
    } else {
        Text("Waiting for microphone permission...")
    }
}