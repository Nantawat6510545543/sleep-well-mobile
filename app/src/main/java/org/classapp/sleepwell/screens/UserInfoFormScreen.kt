package org.classapp.sleepwell.screens

import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

// TODO Refactor
@Composable
fun UserInfoFormScreen(onSubmit: () -> Unit) {
    var age by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }

    var genderMenuExpanded by remember { mutableStateOf(false) }
    val genderOptions = listOf("Male", "Female", "Others")

    val context = LocalContext.current
    val firestore = FirebaseFirestore.getInstance()
    val userId = FirebaseAuth.getInstance().currentUser?.uid

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = "Please fill in the user data.",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
        )
        Text(text = "The data will be used for analytics purpose only.")

        OutlinedTextField(
            value = age,
            onValueChange = { age = it },
            label = { Text("Age") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        OutlinedTextField(
            value = height,
            onValueChange = { height = it },
            label = { Text("Height (cm)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        OutlinedTextField(
            value = weight,
            onValueChange = { weight = it },
            label = { Text("Weight (kg)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        // Gender Dropdown Selector
        Box(
            modifier = Modifier
                .border(1.dp, Color.Black, MaterialTheme.shapes.small)
                .clickable { genderMenuExpanded = true }
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (gender.isNotBlank()) gender else "Select Gender",
                )
                Icon(Icons.Default.ArrowDropDown, contentDescription = "Select Gender")
            }

            DropdownMenu(
                expanded = genderMenuExpanded,
                onDismissRequest = { genderMenuExpanded = false }
            ) {
                genderOptions.forEach { genderOption ->
                    DropdownMenuItem(
                        text = { Text(genderOption) },
                        onClick = {
                            gender = genderOption
                            genderMenuExpanded = false
                        }
                    )
                }
            }
        }

        Button(
            onClick = {
                val ageInt = age.toIntOrNull()
                val heightInt = height.toIntOrNull()
                val weightInt = weight.toIntOrNull()

                if (userId != null &&
                    ageInt != null && heightInt != null
                    && weightInt != null && gender.isNotBlank()
                ) {
                    val userProfile = mapOf(
                        "age" to ageInt,
                        "height" to heightInt,
                        "weight" to weightInt,
                        "gender" to gender
                    )

                    firestore.collection("profiles")
                        .document(userId)
                        .set(userProfile)
                        .addOnSuccessListener {
                            Toast.makeText(context, "Profile saved!", Toast.LENGTH_SHORT).show()
                            onSubmit()
                        }
                        .addOnFailureListener {
                            Toast.makeText(context, "Failed to save data.", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Toast.makeText(
                        context, "Invalid or incomplete input. Check all fields and try again.", Toast.LENGTH_SHORT).show()
                }
            }
        ) {
            Text("Continue")
        }
    }
}