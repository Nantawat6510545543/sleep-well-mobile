package org.classapp.sleepwell.screens

import android.Manifest
import androidx.compose.foundation.layout.*
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Mic
//import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import org.classapp.sleepwell.components.DecibelMeterSection
import org.classapp.sleepwell.components.UserInfoSection
import org.classapp.sleepwell.utils.*

@Composable
fun HomeScreen() {
    val context = LocalContext.current
    requestPermission(
        listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.RECORD_AUDIO
        )
    )

    val locationPermissionGranted = hasPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
    val audioPermissionGranted = hasPermission(context, Manifest.permission.RECORD_AUDIO)

    val location = getUserLocation(locationPermissionGranted)
    val user = FirebaseAuth.getInstance().currentUser
    var weatherData by remember { mutableStateOf<FlattenedWeatherApiResponse?>(null) }
    val coroutineScope = rememberCoroutineScope()

    var isRecording by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        user?.let {
            UserInfoSection(user = it, location = location)
        }

        Button(
            onClick = {
                coroutineScope.launch {
                    val weather = location?.let { fetchWeatherResponse(it) }
                    weatherData = weather?.let { flattenedWeatherResponse(it) }
                }
            }
        ) {
            Text("Get Weather")
        }

        weatherData?.let {
            WeatherDisplay(weather = it)
        } ?: Text("No weather data available.", fontSize = 16.sp)

        Spacer(modifier = Modifier.height(16.dp))

        // Start/Stop Recording Button with Icon
        Button(
            onClick = { isRecording = !isRecording },
            enabled = audioPermissionGranted
        ) {
//            Icon(
//                imageVector = if (isRecording) Icons.Filled.Stop else Icons.Filled.Mic,
//                contentDescription = if (isRecording) "Stop Recording" else "Start Recording"
//            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(if (isRecording) "Stop Recording" else "Start Recording")
        }

        DecibelMeterSection(
            audioPermissionGranted = audioPermissionGranted,
            recording = isRecording
        )
    }
}
