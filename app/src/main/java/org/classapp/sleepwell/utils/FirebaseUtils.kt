package org.classapp.sleepwell.utils

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

suspend fun fetchSleepLog(userId: String): List<SleepLog> {
    val firestore = Firebase.firestore
    return try {
        val snapshot = firestore.collection("sleeps")
            .whereEqualTo("userId", userId)
            .orderBy("sleepTime", Query.Direction.DESCENDING)
            .get()
            .await()

        snapshot.documents.mapNotNull { it.toObject(SleepLog::class.java) }
    } catch (e: Exception) {
        e.printStackTrace()
        emptyList()
    }
}

suspend fun fetchLatestSleepLog(userId: String): SleepLog? {
    return fetchSleepLog(userId).firstOrNull()
}

fun deleteSleepLog(sleepId: String) {
    val db = FirebaseFirestore.getInstance()
    val user = FirebaseAuth.getInstance().currentUser

    if (user != null) {
        db.collection("sleeps")
            .document(sleepId)
            .delete()
            .addOnSuccessListener {
                Log.d("FirestoreUtils", "Sleep log deleted successfully.")
            }
            .addOnFailureListener { exception ->
                Log.d("FirestoreUtils", "Error deleting sleep log: ", exception)
            }
    } else {
        Log.d("FirestoreUtils", "User is not authenticated.")
    }
}

suspend fun getSleepLogById(sleepId: String): SleepLog? {
    val db = Firebase.firestore
    return try {
        val doc = db.collection("sleeps")
            .document(sleepId)
            .get()
            .await()
        doc.toObject(SleepLog::class.java)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

suspend fun getUserInfo(userId: String): UserInfo? {
    val firestore = FirebaseFirestore.getInstance()

    return try {
        val documentSnapshot = firestore.collection("profiles")
            .document(userId)
            .get()
            .await()

        // Convert Firestore document to UserInfo object
        documentSnapshot.toObject(UserInfo::class.java)
    } catch (e: Exception) {
        Log.e("getUserInfo", "Error fetching user data: ${e.message}")
        null
    }
}
