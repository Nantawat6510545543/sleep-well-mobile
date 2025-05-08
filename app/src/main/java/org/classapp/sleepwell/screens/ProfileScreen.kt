package org.classapp.sleepwell.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.classapp.sleepwell.components.EditableField
import org.classapp.sleepwell.navigations.Routes
import org.classapp.sleepwell.utils.ProfileImage
import org.classapp.sleepwell.utils.updateFirebaseAuthProfile

@Composable
fun ProfileScreen(navController: NavController) {
    val user = FirebaseAuth.getInstance().currentUser

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Your Profile",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (user != null) {
            val userId = user.uid
            val email = user.email

            // UI state
            var username by remember { mutableStateOf(user.displayName ?: "") }
            var age by remember { mutableStateOf<String?>(null) }
            var gender by remember { mutableStateOf<String?>(null) }
            var height by remember { mutableStateOf<String?>(null) }
            var weight by remember { mutableStateOf<String?>(null) }
            var photoUrl by remember { mutableStateOf(user.photoUrl?.toString()) } // Ensure initial photo URL
            var editMode by remember { mutableStateOf(false) }

            // Image picker for external hosted URLs
            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.GetContent()
            ) { uri: Uri? ->
                uri?.let {
                    photoUrl = it.toString() // Update photo URL when a new image is picked
                }
            }

            // Load Firestore profile
            LaunchedEffect(userId) {
                user.reload() // Refresh user profile from Firebase
                val firestore = FirebaseFirestore.getInstance()
                firestore.collection("profiles")
                    .document(userId)
                    .get()
                    .addOnSuccessListener { doc ->
                        username = doc.getString("username") ?: username
                        age = doc.getString("age")
                        gender = doc.getString("gender")
                        height = doc.getString("height")
                        weight = doc.getString("weight")
                        photoUrl = doc.getString("photoUrl") ?: photoUrl
                    }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.Center)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    ProfileImage(photoUrl)

                    // Image picker button
                    if (editMode) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { launcher.launch("image/*") }) {
                            Text("Choose New Profile Picture")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text("User ID: $userId", modifier = Modifier.padding(vertical = 4.dp))
            Text("Email: $email", modifier = Modifier.padding(vertical = 4.dp))

            EditableField("Username", username, editMode) { username = it }
            EditableField("Age", age, editMode) { age = it }
            EditableField("Gender", gender, editMode) { gender = it }
            EditableField("Height", height, editMode) { height = it }
            EditableField("Weight", weight, editMode) { weight = it }
            EditableField("Photo URL", photoUrl, editMode) { photoUrl = it }

            Spacer(modifier = Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (editMode) {
                    Button(onClick = {
                        val firestore = FirebaseFirestore.getInstance()
                        val profileUpdates = mapOf(
                            "username" to username,
                            "age" to age,
                            "gender" to gender,
                            "height" to height,
                            "weight" to weight,
                            "photoUrl" to photoUrl
                        )

                        firestore.collection("profiles").document(userId)
                            .set(profileUpdates.filterValues { it != null }) // full overwrite
                            .addOnSuccessListener {
                                // Sync with Firebase Auth
                                updateFirebaseAuthProfile(username, photoUrl)
                                editMode = false
                            }
                    }) {
                        Text("Save")
                    }
                } else {
                    Button(onClick = { editMode = true }) {
                        Text("Edit Info")
                    }
                }

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
}
