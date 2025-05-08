package org.classapp.sleepwell.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.classapp.sleepwell.navigations.Routes
import org.classapp.sleepwell.utils.ProfileImage

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

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.Center)
            ) {
                ProfileImage(user.photoUrl.toString())
            }

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