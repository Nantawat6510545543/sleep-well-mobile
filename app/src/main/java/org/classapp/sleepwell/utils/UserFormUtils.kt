package org.classapp.sleepwell.utils

import android.content.Context
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth

// Data class to hold user information
data class UserInfo(
    val gender: String? = null,
    val age: Int? = null,
    val height: Int? = null,
    val weight: Double? = null
)

// Validation function to check if the user info is complete
fun validateUserInfo(userInfo: UserInfo): Boolean {
    return userInfo.age != null && userInfo.height != null && userInfo.weight != null && !userInfo.gender.isNullOrBlank()
}

// Function to save user profile to Firestore
fun saveUserProfile(
    userInfo: UserInfo,
    context: Context,
    onSubmit: () -> Unit
) {
    if (validateUserInfo(userInfo)) {
        val userProfile = mapOf(
            "age" to userInfo.age,
            "height" to userInfo.height,
            "weight" to userInfo.weight,
            "gender" to userInfo.gender
        )

        val userId = FirebaseAuth.getInstance().currentUser?.uid

        if (userId != null) {
            FirebaseFirestore.getInstance()
                .collection("profiles")
                .document(userId)
                .set(userProfile)
                .addOnSuccessListener {
                    Toast.makeText(context, "Profile saved!", Toast.LENGTH_SHORT).show()
                    onSubmit()
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Failed to save data.", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(context, "User not authenticated.", Toast.LENGTH_SHORT).show()
        }
    } else {
        Toast.makeText(context, "Invalid or incomplete input. Check all fields and try again.", Toast.LENGTH_SHORT).show()
    }
}