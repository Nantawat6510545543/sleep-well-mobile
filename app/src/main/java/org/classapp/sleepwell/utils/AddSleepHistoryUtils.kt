package org.classapp.sleepwell.utils

data class ValidationResult(
    val isValid: Boolean,
    val message: String = ""
)

data class SleepLog(
    val dateTime: String,
    val duration: Int,
    val quality: String,
)

fun validateSleepInput(
    dateTime: String,
    duration: String,
    quality: String,
    consentChecked: Boolean
): ValidationResult {
    if (dateTime == "Press here to pick date & time") {
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

fun handleConfirmClick(
    dateTime: String,
    duration: String,
    quality: String,
) {
    // You can add validation, analytics, saving to DB, etc.
    val sleepLog = SleepLog(
        dateTime = dateTime,
        duration = duration.toInt(),
        quality = quality,
    )

    // Replace with your desired logic
    println("Collected sleep log: $sleepLog")
}
