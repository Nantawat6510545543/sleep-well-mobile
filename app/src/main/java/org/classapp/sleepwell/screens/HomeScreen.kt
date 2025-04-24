package org.classapp.sleepwell.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import org.classapp.sleepwell.utils.FlattenedWeatherApiResponse
import org.classapp.sleepwell.utils.rememberLocation
import org.classapp.sleepwell.utils.fetchWeatherResponse
import org.classapp.sleepwell.utils.flattenedWeatherResponse


@Composable
fun HomeScreen() {
    val location = rememberLocation() // helper state management for geolocation
    val user = FirebaseAuth.getInstance().currentUser
    var flattenedWeather by remember { mutableStateOf<FlattenedWeatherApiResponse?>(null) }
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        if (user != null) {
            Text(
                text = "Welcome, ${user.displayName}",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Text(user.uid)

            location?.let {
                Text("Latitude: ${location.latitude}")
                Text("Longitude: ${location.longitude}")
            } ?: Text("Fetching location...")
        }

        Button(
            onClick = {
                coroutineScope.launch {
                   val weather = location?.let { fetchWeatherResponse(it) }
                   flattenedWeather = weather?.let { flattenedWeatherResponse(it) }
                }
            }
        ) {
            Text("Get Weather")
        }

        // Show weather data if available
        flattenedWeather?.let {
            Column(modifier = Modifier.padding(top = 16.dp)) {
                Text("Temperature: ${it.temp_c}°C", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text("Condition: ${it.condition_text}", fontSize = 16.sp)
                Text("Precipitation: ${it.precip_mm} mm", fontSize = 16.sp)
                Text("Humidity: ${it.humidity}%", fontSize = 16.sp)
                Text("Country: ${it.country}", fontSize = 16.sp)
                Text("Location: ${it.location_name}, ${it.region}", fontSize = 16.sp)
                Text("Timezone: ${it.tz_id}", fontSize = 16.sp)
                Text("Timestamp: ${it.timestamp}", fontSize = 16.sp)
            }
        } ?: Text("No weather data available.", fontSize = 16.sp)
    }
}