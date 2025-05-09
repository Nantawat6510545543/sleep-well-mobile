package org.classapp.sleepwell.utils

import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

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

suspend fun queryUserSleepDataSuspend(userId: String): List<Map<String, Any>> {
    val db = FirebaseFirestore.getInstance()
    val snapshot = db.collection("sleeps")
        .whereEqualTo("userId", userId)
        .get()
        .await()
    return snapshot.map { it.data }
}

fun calculateAverageSleepScore(data: List<Map<String, Any>>): Double {
    val scores = data.mapNotNull { (it["sleepScore"] as? Number)?.toDouble() }
    return if (scores.isNotEmpty()) scores.average() else 0.0
}

fun calculateAverageDuration(data: List<Map<String, Any>>): Double {
    val durations = data.mapNotNull { (it["duration"] as? Number)?.toDouble() }
    return if (durations.isNotEmpty()) durations.average() else 0.0
}