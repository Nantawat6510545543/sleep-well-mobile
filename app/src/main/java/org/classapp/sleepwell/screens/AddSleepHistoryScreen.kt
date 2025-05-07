package org.classapp.sleepwell.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.classapp.sleepwell.components.DateTimePickerButton
import org.classapp.sleepwell.components.DateTimePickerDialog

@Composable
fun AddSleepHistoryScreen() {
    var sleepDateTime by remember { mutableStateOf("Press here to pick date & time") }
    var sleepDuration by remember { mutableStateOf("") }
    var sleepQuality by remember { mutableStateOf("") }
    var consentChecked by remember { mutableStateOf(false) }
    var showDateTimePicker by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = "Add New Sleep Log",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(text = "When did you go to sleep?")
            Spacer(modifier = Modifier.height(2.dp))
            DateTimePickerButton (
                text = sleepDateTime,
                onClick = { showDateTimePicker = true }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "How long did you sleep?")
            Spacer(modifier = Modifier.height(2.dp))
            OutlinedTextField(
                value = sleepDuration,
                onValueChange = { sleepDuration = it },
                placeholder = { Text("Input text") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "How was your sleep?")
            Spacer(modifier = Modifier.height(2.dp))
            OutlinedTextField(
                value = sleepQuality,
                onValueChange = { sleepQuality = it },
                placeholder = { Text("Input text") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.Top) {
                Checkbox(
                    checked = consentChecked,
                    onCheckedChange = { consentChecked = it }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "By clicking, you consent to us collecting your location and noise data"
                )
            }
        }

        Button(
            onClick = { /* Handle confirm action */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5E3BEE))
        ) {
            Text(text = "Confirm", color = Color.White)
        }
    }

    if (showDateTimePicker) {
        DateTimePickerDialog(
            onDismiss = { showDateTimePicker = false },
            onConfirm = { dateTime ->
                sleepDateTime = dateTime
                showDateTimePicker = false
            }
        )
    }
}