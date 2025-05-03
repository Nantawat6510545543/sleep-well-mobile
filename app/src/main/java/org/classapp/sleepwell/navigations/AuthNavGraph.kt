package org.classapp.sleepwell.navigations

import android.app.Activity
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.classapp.sleepwell.screens.AuthenticationScreen
import org.classapp.sleepwell.screens.UserInfoFormScreen

fun NavGraphBuilder.authNavGraph(navController: NavController) {
    composable(Routes.SIGN_IN) {
        val context = LocalContext.current
        AuthenticationScreen { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val userId = FirebaseAuth.getInstance().currentUser?.uid
                val firestore = FirebaseFirestore.getInstance()

                if (userId != null) {
                    firestore.collection("profiles")
                        .document(userId)
                        .get()
                        .addOnSuccessListener { document ->
                            if (document.exists()) {
                                // Profile exists → Go to MainScreen
                                navController.navigate(Routes.MAIN) {
                                    popUpTo(Routes.SIGN_IN) { inclusive = true }
                                }
                            } else {
                                // Profile does NOT exist → Go to profile form
                                navController.navigate(Routes.USER_INFO_FORM) {
                                    popUpTo(Routes.SIGN_IN) { inclusive = true }
                                }
                            }
                        }
                        .addOnFailureListener {
                            Toast.makeText(context, "Error loading profile", Toast.LENGTH_SHORT).show()
                        }
                }
            } else {
                val response = result.idpResponse
                val message = if (response == null) {
                    "Sign-in canceled."
                } else {
                    "Sign-in failed: ${response.error?.errorCode}"
                }
                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            }
        }
    }

    composable(Routes.USER_INFO_FORM) {
        UserInfoFormScreen {
            navController.navigate(Routes.MAIN) {
                popUpTo(Routes.USER_INFO_FORM) { inclusive = true }
            }
        }
    }
}