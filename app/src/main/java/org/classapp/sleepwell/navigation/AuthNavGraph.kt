package org.classapp.sleepwell.navigation

import android.app.Activity
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import org.classapp.sleepwell.screens.AuthenticationScreen

fun NavGraphBuilder.authNavGraph(navController: NavController) {
    composable(Routes.SIGN_IN) {
        val context = LocalContext.current
        AuthenticationScreen { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                navController.navigate(Routes.MAIN) {
                    popUpTo(Routes.SIGN_IN) { inclusive = true }
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
}