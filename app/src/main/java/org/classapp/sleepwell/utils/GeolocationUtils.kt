package org.classapp.sleepwell.utils

import android.location.Location
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.tasks.await

@Composable
fun getUserLocation(hasGeoPermission: Boolean): Location? {
    val context = LocalContext.current
    var location by remember { mutableStateOf<Location?>(null) }

    // Fetch location if permission granted
    LaunchedEffect(hasGeoPermission) {
        if (hasGeoPermission) {
            try {
                val fusedClient = LocationServices.getFusedLocationProviderClient(context)
                location = fusedClient.getCurrentLocation(
                    Priority.PRIORITY_HIGH_ACCURACY, null
                ).await()
            } catch (e: SecurityException) {
                Log.e("GeolocationUtils", "Permission denied during location fetch", e)
            } catch (e: Exception) {
                Log.e("GeolocationUtils", "Failed to fetch location", e)
            }
        }
    }
    return location
}
