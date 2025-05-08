package org.classapp.sleepwell.utils

import android.content.Context
import android.location.Location
import android.util.Log
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.tasks.await

suspend fun getUserLocation(context: Context, hasGeoPermission: Boolean): Location? {
    if (!hasGeoPermission) return null

    return try {
        val fusedClient = LocationServices.getFusedLocationProviderClient(context)
        fusedClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null).await()
    } catch (e: SecurityException) {
        Log.e("GeolocationUtils", "Permission denied during location fetch", e)
        null
    } catch (e: Exception) {
        Log.e("GeolocationUtils", "Failed to fetch location", e)
        null
    }
}
