package org.classapp.sleepwell.screens

import android.Manifest
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import org.classapp.sleepwell.components.DecibelMeterSection
import org.classapp.sleepwell.components.UserInfoSection
import org.classapp.sleepwell.utils.FlattenedWeatherApiResponse
import org.classapp.sleepwell.utils.getUserLocation
import org.classapp.sleepwell.utils.fetchWeatherResponse
import org.classapp.sleepwell.utils.flattenedWeatherResponse
import org.classapp.sleepwell.utils.hasPermission
import org.classapp.sleepwell.utils.requestPermission


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

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

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

        // Show weather data if available
        weatherData?.let {
            WeatherDisplay(weather = it)
        } ?: Text("No weather data available.", fontSize = 16.sp)

        DecibelMeterSection(audioPermissionGranted)
    }
}