package org.classapp.sleepwell.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import org.classapp.sleepwell.navigation.Routes

@Composable
fun ProfileScreen(navController: NavController) {
    val user = FirebaseAuth.getInstance().currentUser

    if (user != null) {
        val displayName = user.displayName ?: "No Username"
        val email = user.email ?: "No Email"
        val photoUrl = user.photoUrl?.toString()

        // Displaying info in Compose UI
        Column {
            Text(text = "Username: $displayName")
            Text(text = "Email: $email")
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

//        if (photoUrl != null) {
//            Image(
//                painter = rememberAsyncImagePainter(photoUrl),
//                contentDescription = "Profile Picture"
//            )
//        } else {
//            Text(text = "No Profile Picture")
//        }
    }
}