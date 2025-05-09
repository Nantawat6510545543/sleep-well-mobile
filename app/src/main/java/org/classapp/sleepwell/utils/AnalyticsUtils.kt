package org.classapp.sleepwell.utils

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

data class SleepEntry(
    val sleepScore: Float,
    val sleepTime: Timestamp
)

// Analytics Page Sleep Data
suspend fun fetchSleepAnalyticsData(userId: String): List<SleepEntry> {
    val snapshot = FirebaseFirestore.getInstance()
        .collection("sleeps")
        .whereEqualTo("userId", userId)
        .orderBy("sleepTime")
        .get()
        .await()

    return snapshot.mapNotNull { doc ->
        val score = doc.getDouble("sleepScore")?.toFloat()
        val time = doc.getTimestamp("sleepTime")
        if (score != null && time != null) SleepEntry(score, time) else null
    }
}

fun calculateAverageSleepScore(sleepLogs: List<SleepLog>): Double {
    val scores = sleepLogs.map { it.sleepScore }
    return if (scores.isNotEmpty()) scores.average() else 0.0
}

fun calculateAverageDuration(sleepLogs: List<SleepLog>): Double {
    val durations = sleepLogs.map { it.duration }
    return if (durations.isNotEmpty()) durations.average() else 0.0
}