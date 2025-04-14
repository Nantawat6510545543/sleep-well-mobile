package org.classapp.sleepwell

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


sealed class AuthScreen(val route: String) {
    data object SignIn : AuthScreen("sign_in")
    data object Home : AuthScreen("home")
}

@Composable
fun AuthNavigation() {
    val navController = rememberNavController()
    val authStatus = remember { mutableStateOf("Waiting for authentication...") }

    NavHost(navController = navController, startDestination = AuthScreen.SignIn.route) {
        composable(AuthScreen.SignIn.route) {
            AuthenticationScreen { result ->
                if (result.resultCode == android.app.Activity.RESULT_OK) {
                    authStatus.value = "Authenticated!"
                    navController.navigate(AuthScreen.Home.route) {
                        popUpTo(AuthScreen.SignIn.route) { inclusive = true } // prevent going back
                    }
                }
                else {
                    // Handle failure or cancellation
                    val response = result.idpResponse
                    if (response == null) {
                        authStatus.value = "Sign-in canceled. Please try again."
                    } else {
                        val errorCode = response.error?.errorCode
                        authStatus.value = "Sign-in failed. Error code: $errorCode"
                    }
                }
            }
        }

        composable(AuthScreen.Home.route) {
            HomeScreen()
        }
    }

    Text(text = authStatus.value, color = Color.Red)
}