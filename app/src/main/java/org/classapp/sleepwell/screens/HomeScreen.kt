package org.classapp.sleepwell.screens

import android.Manifest
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import org.classapp.sleepwell.utils.rememberLocation
import org.classapp.sleepwell.utils.fetchWeatherResponse
import org.classapp.sleepwell.utils.flattenedWeatherResponse


@Composable
fun HomeScreen() {
    val location = rememberLocation() // helper state management for geolocation
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

        DecibelMeterSection()
    }
}