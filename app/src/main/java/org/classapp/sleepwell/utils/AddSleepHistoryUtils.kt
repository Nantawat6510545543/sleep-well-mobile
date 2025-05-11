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
    // Sleep
    val userId: String = "",
    val sleepTime: Timestamp = Timestamp.now(),
    val duration: Double = 0.0,
    val sleepComment: String = "",
    val sleepScore: Double = 0.0,

    // Weather
    val place: String = "",
    val tempC: Double = 0.0,
    val humidity: Int = 0,
    val precip: Double = 0.0,
    val weatherCondition: String = "",
    val noise: Double = 0.0
)

fun convertToTimestamp(sleepTime: String): Timestamp? {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
    return try {
        val parsedDate: Date? = dateFormat.parse(sleepTime)
        parsedDate?.let { Timestamp(it) }
    } catch (e: Exception) {
        Log.e("convertToTimestamp", "Error converting date")
        null
    }
}

fun validateSleepInput(
    sleepTime: String,
    duration: String,
    sleepComment: String,
    consentChecked: Boolean
): ValidationResult {
    if (sleepTime == "Press here to pick date & time") {
        return ValidationResult(false, "Please select a valid date and time.")
    }

    val durationDouble = duration.toDoubleOrNull()
    if (durationDouble == null || durationDouble <= 0) {
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
    sleepTime: String,
    duration: String,
    sleepComment: String,
    hasGeoPermission: Boolean,
    averageDecibel: Double
) {
    val location = getUserLocation(context, hasGeoPermission)
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    val weatherResponse = location?.let { fetchWeatherResponse(it) }
    val sleepTimestamp = convertToTimestamp(sleepTime)
    val userInfo = userId?.let { getUserInfo(it) }

    if (location != null && userId != null && weatherResponse != null
        && sleepTimestamp != null && userInfo != null) {
        val flattened = flattenedWeatherResponse(weatherResponse)

        val genderInt = when (userInfo.gender) {
            "Male" -> 0
            "Female" -> 1
            else -> 2 // For "Others" or any other gender
        }

        val rawFeatures = listOf(
            genderInt.toFloat(),
            userInfo.age!!.toFloat(),
            userInfo.height!!.toFloat(),
            userInfo.weight!!.toFloat(),
            flattened.temp_c.toFloat(),
            encodeWeatherCondition(context, flattened.condition_text).toFloat(),
            flattened.precip_mm.toFloat(),
            flattened.humidity.toFloat(),
            averageDecibel.toFloat(),
            duration.toFloat(),
            predictSentiment(context, sleepComment).toFloat()
        )

        val scalerParams = loadScalerParamsFromAssets(context)
        val standardizedFeatures = standardizeFeatures(rawFeatures, scalerParams)

        Log.e("feature", standardizedFeatures.toString())

        val sleepLog = SleepLog(
            userId = userId,
            sleepTime = sleepTimestamp,
            duration = duration.toDouble(),
            sleepComment = sleepComment,
            noise = averageDecibel,
            sleepScore = predictSleepScore(context, standardizedFeatures),
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