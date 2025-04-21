package org.classapp.sleepwell.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.google.firebase.auth.FirebaseAuth
import org.classapp.sleepwell.navigation.Routes

@Composable
fun ProfileScreen(navController: NavController) {
    val user = FirebaseAuth.getInstance().currentUser

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        // Title Text
        Text(
            text = "Your Profile",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = androidx.compose.ui.graphics.Color.Black,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (user != null) {
            val displayName = user.displayName ?: "No Username"
            val email = user.email ?: "No Email"
//            val photoUrl = user.photoUrl.toString()
//            Log.d("DEBUG", photoUrl)
//            // TODO add default profile picture
//            AsyncImage(
//                model = "https://lh3.googleusercontent.com/a/ACg8ocLTLMOkOFe3E1MNazpp_uh0w6H9YTtMWMgl674T8n5xxHHHDA=s96-c",
//                contentDescription = "Your Profile Picture",
//            )

            Text(text = "Username: $displayName")
            Text(text = "Email: $email")
            Button(onClick = {
                FirebaseAuth.getInstance().signOut()
                navController.navigate(Routes.SIGN_IN) {
                    popUpTo(Routes.MAIN) { inclusive = true }
                }
            }) {
                Text("Logout")
            }
        }
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
