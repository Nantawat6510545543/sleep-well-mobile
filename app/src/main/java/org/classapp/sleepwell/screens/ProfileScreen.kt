package org.classapp.sleepwell.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
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

    user?.let {
        val userId = user.uid
        val email = user.email

        var username by remember { mutableStateOf(user.displayName ?: "") }
        var age by remember { mutableStateOf<String?>(null) }
        var gender by remember { mutableStateOf<String?>(null) }
        var height by remember { mutableStateOf<String?>(null) }
        var weight by remember { mutableStateOf<String?>(null) }
        var photoUrl by remember { mutableStateOf(user.photoUrl?.toString()) }
        var editMode by remember { mutableStateOf(false) }

        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            uri?.let {
                photoUrl = it.toString()
            }
        }

        LaunchedEffect(userId) {
            user.reload()
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

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    if (editMode) {
                        Button(onClick = {
                            val profileUpdates = mapOf(
                                "username" to username,
                                "age" to age?.toIntOrNull(),
                                "gender" to gender,
                                "height" to height?.toFloatOrNull(),
                                "weight" to weight?.toFloatOrNull(),
                                "photoUrl" to photoUrl
                            )

                            FirebaseFirestore.getInstance().collection("profiles").document(userId)
                                .set(profileUpdates.filterValues { it != null })
                                .addOnSuccessListener {
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
                    Spacer(modifier = Modifier.width(16.dp))
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
        ) { innerPadding ->
            LazyColumn(
                contentPadding = innerPadding,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .imePadding()
            ) {
                item {
                    Text(
                        text = "Your Profile",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                }

                item {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            ProfileImage(photoUrl)
                            if (editMode) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Button(onClick = { launcher.launch("image/*") }) {
                                    Text("Choose New Profile Picture")
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("User ID: $userId", modifier = Modifier.padding(vertical = 4.dp))
                            Text("Email: $email", modifier = Modifier.padding(vertical = 4.dp))
                            EditableField("Username", username, editMode) { username = it }
                            EditableField("Age", age, editMode) { age = it }
                            EditableField("Gender", gender, editMode) { gender = it }
                            EditableField("Height", height, editMode) { height = it }
                            EditableField("Weight", weight, editMode) { weight = it }
                            EditableField("Photo URL", photoUrl, editMode) { photoUrl = it }
                        }
                    }
                    Spacer(modifier = Modifier.height(100.dp)) // So content isn't blocked by bottom bar
                }
            }
        }
    }
}
