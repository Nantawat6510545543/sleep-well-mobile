package org.classapp.sleepwell.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.classapp.sleepwell.navigations.Routes

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
            // TODO refactor
            var age by remember { mutableStateOf<String?>(null) }
            var height by remember { mutableStateOf<String?>(null) }
            var weight by remember { mutableStateOf<String?>(null) }
            var gender by remember { mutableStateOf<String?>(null) }

            val userId = user.uid
            val displayName = user.displayName ?: "No Username"
            val email = user.email ?: "No Email"

            LaunchedEffect(userId) {
                val firestore = FirebaseFirestore.getInstance()
                firestore.collection("profiles")
                    .document(userId)
                    .get()
                    .addOnSuccessListener { doc ->
                        age = doc.get("age")?.toString()
                        gender = doc.get("gender")?.toString()
                        height = doc.get("height")?.toString()
                        weight = doc.get("weight")?.toString()
                    }
            }

//            val photoUrl = user.photoUrl.toString()
//            Log.d("DEBUG", photoUrl)
//            // TODO add default profile picture
//            AsyncImage(
//                model = "https://lh3.googleusercontent.com/a/ACg8ocLTLMOkOFe3E1MNazpp_uh0w6H9YTtMWMgl674T8n5xxHHHDA=s96-c",
//                contentDescription = "Your Profile Picture",
//            )

            Text("User ID: $userId")
            Text("Username: $displayName")
            Text("Email: $email")
            Text("Age: $age")
            Text("Gender: $gender")
            Text("Height: $height cm")
            Text("Weight: $weight kg")
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
