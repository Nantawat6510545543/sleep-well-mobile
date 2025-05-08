package org.classapp.sleepwell.utils

import android.content.Context
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class ValidationResult(
    val isValid: Boolean,
    val message: String = ""
)

data class SleepLog(
    val userId: String,
    val sleepDate: Timestamp,
    val duration: Int,
    val sleepComment: String,
    val sleepScore: Int,

    val place: String,
    val tempC: Double,
    val humidity: Int,
    val precip: Double,
    val weatherCondition: String,
    val noise: Double
)

fun convertToTimestamp(sleepDate: String): Timestamp? {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
    return try {
        val parsedDate: Date? = dateFormat.parse(sleepDate)
        parsedDate?.let { Timestamp(it) }
    } catch (e: Exception) {
        Log.e("convertToTimestamp", "Error converting date")
        null
    }
}

fun validateSleepInput(
    sleepDate: String,
    duration: String,
    sleepComment: String,
    consentChecked: Boolean
): ValidationResult {
    if (sleepDate == "Press here to pick date & time") {
        return ValidationResult(false, "Please select a valid date and time.")
    }

    val durationInt = duration.toIntOrNull()
    if (durationInt == null || durationInt <= 0) {
        return ValidationResult(false, "Please enter a valid sleep duration (as a number).")
    }

    if (sleepComment.isBlank()) {
        return ValidationResult(false, "Please describe your sleep quality.")
    }

    if (!consentChecked) {
        return ValidationResult(false, "You must agree to the consent to proceed.")
    }

    return ValidationResult(true)
}


suspend fun handleConfirmClick(
    context: Context,
    sleepDate: String,
    duration: String,
    sleepComment: String,
    hasGeoPermission: Boolean,
    averageDecibel: Double
) {
    val location = getUserLocation(context, hasGeoPermission)
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    val weatherResponse = location?.let { fetchWeatherResponse(it) }
    val sleepTimestamp = convertToTimestamp(sleepDate)

    if (location != null && userId != null && weatherResponse != null && sleepTimestamp != null) {
        val flattened = flattenedWeatherResponse(weatherResponse)

        val sleepLog = SleepLog(
            userId = userId,
            sleepDate = sleepTimestamp,
            duration = duration.toInt(),
            sleepComment = sleepComment,
            noise = averageDecibel,
            sleepScore = 123,  // TODO: compute meaningful score
            place = flattened.location_name,
            tempC = flattened.temp_c,
            humidity = flattened.humidity,
            precip = flattened.precip_mm,
            weatherCondition = flattened.condition_text
        )

        // Proceed with Firestore save operation
        println("Collected sleep log: $sleepLog")

        val db = Firebase.firestore
        db.collection("sleeps")
            .add(sleepLog)
            .addOnSuccessListener { documentRef ->
                println("Sleep log added with ID: ${documentRef.id}")
            }
            .addOnFailureListener { e ->
                println("Error adding sleep log: $e")
            }
    } else {
        println("Missing data: Location, user ID, weather data, or sleep timestamp.")
    }
}