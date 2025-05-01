package org.classapp.sleepwell.components

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

@Composable
fun DecibelMeterSection() {
    val context = LocalContext.current
    var audioPermissionGranted by remember { mutableStateOf(false) }

    val audioPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        audioPermissionGranted = isGranted
    }

    // TODO config remember activity to check both audio and location permission
    LaunchedEffect(Unit) {
        val hasAudioPermission = ContextCompat.checkSelfPermission(
            context, Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED
        if (!hasAudioPermission) {
            audioPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        } else {
            audioPermissionGranted = true
        }
    }

    if (audioPermissionGranted) {
        Text("Microphone permission granted. Decibel meter would be active.")
        // Initialize decibel measuring logic here
    } else {
        Text("Waiting for microphone permission...")
    }
}
