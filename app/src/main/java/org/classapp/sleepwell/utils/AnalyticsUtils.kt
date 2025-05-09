package org.classapp.sleepwell.utils

import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

data class SleepEntry(
    val sleepScore: Float,
    val sleepTime: Timestamp
)

suspend fun fetchLatestSleepLog(userId: String): SleepLog? {
    val firestore = Firebase.firestore
    return try {
        val snapshot = firestore.collection("sleeps")
            .whereEqualTo("userId", userId)
            .orderBy("sleepTime", Query.Direction.DESCENDING)
            .limit(1)
            .get()
            .await()

        if (!snapshot.isEmpty) {
            snapshot.documents[0].toObject(SleepLog::class.java)
        } else null
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

// Quick Insight Sleep Data
suspend fun queryUserSleepDataSuspend(userId: String): List<Map<String, Any>> {
    val db = FirebaseFirestore.getInstance()
    val snapshot = db.collection("sleeps")
        .whereEqualTo("userId", userId)
        .orderBy("sleepTime", Query.Direction.DESCENDING)
        .get()
        .await()
    return snapshot.map { it.data }
}

// Analytics Page Sleep Data
fun fetchSleepAnalyticsData(
    userId: String,
    onResult: (List<SleepEntry>) -> Unit
) {
    FirebaseFirestore.getInstance()
        .collection("sleeps") // replace with your actual collection
        .whereEqualTo("userId", userId)
        .orderBy("sleepTime")
        .get()
        .addOnSuccessListener { result ->
            val entries = result.mapNotNull { doc ->
                val score = doc.getDouble("sleepScore")?.toFloat()
                val time = doc.getTimestamp("sleepTime")
                if (score != null && time != null) SleepEntry(score, time) else null
            }
            onResult(entries)
        }
}


fun calculateAverageSleepScore(data: List<Map<String, Any>>): Double {
    val scores = data.mapNotNull { (it["sleepScore"] as? Number)?.toDouble() }
    return if (scores.isNotEmpty()) scores.average() else 0.0
}

fun calculateAverageDuration(data: List<Map<String, Any>>): Double {
    val durations = data.mapNotNull { (it["duration"] as? Number)?.toDouble() }
    return if (durations.isNotEmpty()) durations.average() else 0.0
}