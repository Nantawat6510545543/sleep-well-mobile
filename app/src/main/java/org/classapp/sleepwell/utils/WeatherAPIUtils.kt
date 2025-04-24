package org.classapp.sleepwell.utils

import android.location.Location
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import org.classapp.sleepwell.BuildConfig

interface WeatherApiService {
    @GET("current.json")
    suspend fun getCurrentWeather(
        @Query("key") apiKey: String,
        @Query("q") location: String
    ): WeatherApiResponse
}

data class WeatherApiResponse(
    val location: WeatherLocation,
    val current: Current
)

data class WeatherLocation(
    val name: String,
    val region: String,
    val country: String,
    val lat: Double,
    val lon: Double,
    val tz_id: String,
    val localtime_epoch: Long,
    val localtime: String
)

data class Current(
    val last_updated: String,
    val temp_c: Double,
    val temp_f: Double,
    val is_day: Int,
    val condition: Condition,
    val precip_mm: Double,
    val precip_in: Double,
    val humidity: Int
)

data class Condition(
    val text: String,
    val icon: String,
    val code: Int
)

data class FlattenedWeatherApiResponse(
    val lat: Double,
    val lon: Double,
    val timestamp: Long,
    val temp_c: Double,
    val condition_text: String,
    val precip_mm: Double?,
    val humidity: Int,
    val country: String,
    val location_name: String,
    val region: String,
    val tz_id: String
)

fun createWeatherApi(): WeatherApiService {
    return Retrofit.Builder()
        .baseUrl("https://api.weatherapi.com/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(WeatherApiService::class.java)
}

suspend fun fetchWeatherResponse(location: Location): WeatherApiResponse? {
    val weatherApiKey = BuildConfig.WEATHER_API_KEY

    if (weatherApiKey.isNullOrEmpty()) {
        Log.e("WeatherAPI", "API key is null or empty")
        return null
    }

    try {
        val weatherApi = createWeatherApi()
        val weatherResponse = weatherApi.getCurrentWeather(weatherApiKey, "${location.latitude},${location.longitude}")
        return weatherResponse
    } catch (e: Exception) {
        Log.e("WeatherAPI", "Error fetching weather", e)
        return null
    }
}

fun flattenedWeatherResponse(weatherResponse: WeatherApiResponse): FlattenedWeatherApiResponse {
    // Extract values from the nested location and current objects
    return FlattenedWeatherApiResponse(
        lat = weatherResponse.location.lat,
        lon = weatherResponse.location.lon,
        timestamp = weatherResponse.location.localtime_epoch,
        temp_c = weatherResponse.current.temp_c,
        condition_text = weatherResponse.current.condition.text,
        precip_mm = weatherResponse.current.precip_mm,
        humidity = weatherResponse.current.humidity,
        country = weatherResponse.location.country,
        location_name = weatherResponse.location.name,
        region = weatherResponse.location.region,
        tz_id = weatherResponse.location.tz_id
    )
}