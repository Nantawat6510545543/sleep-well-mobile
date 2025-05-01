package org.classapp.sleepwell.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.*
import org.classapp.sleepwell.utils.FlattenedWeatherApiResponse

@Composable
fun WeatherDisplay(weather: FlattenedWeatherApiResponse) {
    Column(modifier = Modifier.padding(top = 16.dp)) {
        Text("Temperature: ${weather.temp_c}°C", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Text("Condition: ${weather.condition_text}", fontSize = 16.sp)
        Text("Precipitation: ${weather.precip_mm} mm", fontSize = 16.sp)
        Text("Humidity: ${weather.humidity}%", fontSize = 16.sp)
        Text("Country: ${weather.country}", fontSize = 16.sp)
        Text("Location: ${weather.location_name}, ${weather.region}", fontSize = 16.sp)
        Text("Timezone: ${weather.tz_id}", fontSize = 16.sp)
        Text("Timestamp: ${weather.timestamp}", fontSize = 16.sp)
    }
}