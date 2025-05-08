package org.classapp.sleepwell.utils

import android.content.Context

data class ValidationResult(
    val isValid: Boolean,
    val message: String = ""
)

data class SleepLog(
    val sleepDate: String,
    val duration: Int,
    val quality: String,
)

fun validateSleepInput(
    sleepDate: String,
    duration: String,
    quality: String,
    consentChecked: Boolean
): ValidationResult {
    if (sleepDate == "Press here to pick date & time") {
        return ValidationResult(false, "Please select a valid date and time.")
    }

    val durationInt = duration.toIntOrNull()
    if (durationInt == null || durationInt <= 0) {
        return ValidationResult(false, "Please enter a valid sleep duration (as a number).")
    }

    if (quality.isBlank()) {
        return ValidationResult(false, "Please describe your sleep quality.")
    }

    if (!consentChecked) {
        return ValidationResult(false, "You must agree to the consent to proceed.")
    }

    return ValidationResult(true)
}


suspend fun handleConfirmClick(
    context: Context,
    dateTime: String,
    duration: String,
    quality: String,
    hasGeoPermission: Boolean
) {
    val location = if (hasGeoPermission) getUserLocation(context, hasGeoPermission) else null
    val sleepLog = SleepLog(
        sleepDate = dateTime,
        duration = duration.toInt(),
        quality = quality,
    )

    println("Collected sleep log: $sleepLog")

    location?.let {
        val weatherResponse = fetchWeatherResponse(it)
        weatherResponse?.let { response ->
            val flattened = flattenedWeatherResponse(response)
            println("Weather at sleep time: $flattened")
        }
    }
}