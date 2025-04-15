package org.classapp.sleepwell.screens

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import org.classapp.sleepwell.navigation.Routes

@Composable
fun ProfileScreen(navController: NavController) {
    Text(
        text = "Profile Screen",
    )
    Button(onClick = {
        FirebaseAuth.getInstance().signOut()
        navController.navigate(Routes.SIGN_IN) {
            popUpTo(Routes.MAIN) { inclusive = true }
        }
    }) {
        Text("Logout")
    }
}