package org.classapp.sleepwell.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.classapp.sleepwell.components.DropdownSelector
import org.classapp.sleepwell.components.NumericInputField
import org.classapp.sleepwell.utils.UserInfo
import org.classapp.sleepwell.utils.saveUserProfile

@Composable
fun UserInfoFormScreen(onSubmit: () -> Unit) {
    var age by remember { mutableStateOf<Int?>(null) }
    var height by remember { mutableStateOf<Int?>(null) }
    var weight by remember { mutableStateOf<Int?>(null) }
    var gender by remember { mutableStateOf("") }

    val genderOptions = listOf("Male", "Female", "Others")
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = "Please fill in the user data.",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
        )
        Text(text = "The data will be used for analytics purpose only.")

        NumericInputField(label = "Age", value = age, onValueChange = { age = it })
        NumericInputField(label = "Height (cm)", value = height, onValueChange = { height = it })
        NumericInputField(label = "Weight (kg)", value = weight, onValueChange = { weight = it })

        DropdownSelector(
            options = genderOptions,
            selectedOption = gender.takeIf { it.isNotBlank() },
            onOptionSelected = { gender = it },
            label = "Select Gender"
        )

        Button(
            onClick = {
                val userInfo = UserInfo(age, height, weight, gender)
                saveUserProfile(userInfo, context, onSubmit)
            }
        ) {
            Text("Continue")
        }
    }
}
