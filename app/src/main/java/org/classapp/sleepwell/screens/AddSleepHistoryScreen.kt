package org.classapp.sleepwell.screens

import android.Manifest
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.classapp.sleepwell.components.DateTimePickerButton
import org.classapp.sleepwell.components.DateTimePickerDialog
import org.classapp.sleepwell.components.DecibelMeterSection
import org.classapp.sleepwell.navigations.Routes
import org.classapp.sleepwell.utils.handleConfirmClick
import org.classapp.sleepwell.utils.hasPermission
import org.classapp.sleepwell.utils.validateSleepInput

@Composable
fun AddSleepHistoryScreen(navController: NavController) {
    var sleepDateTime by remember { mutableStateOf("Press here to pick date & time") }
    var sleepDuration by remember { mutableStateOf("") }
    var sleepComment by remember { mutableStateOf("") }
    var showDateTimePicker by remember { mutableStateOf(false) }

    var consentChecked by remember { mutableStateOf(false) }
    var showValidationError by remember { mutableStateOf(false) }
    var validationMessage by remember { mutableStateOf("") }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val locationPermissionGranted = hasPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
    val audioPermissionGranted = hasPermission(context, Manifest.permission.RECORD_AUDIO)

    var averageDecibel by remember { mutableStateOf<Double?>(null) }
    var isRecording by remember { mutableStateOf(false) }
    var hasRecorded by remember { mutableStateOf(false) }
    var recordingSavedMessage by remember { mutableStateOf(false) }

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
            DateTimePickerButton(
                text = sleepDateTime,
                onClick = { showDateTimePicker = true }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "How long did you sleep (hours)?")
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
                value = sleepComment,
                onValueChange = { sleepComment = it },
                placeholder = { Text("Input text") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = consentChecked,
                    onCheckedChange = { consentChecked = it }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "By clicking, you consent to us collecting your location and noise data for analytics"
                )
            }

            if (showValidationError) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = validationMessage,
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        // Recording Controls
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Press this before you sleep, and press again to stop recording to collect noise data",
                modifier = Modifier.padding(bottom = 16.dp),
                style = MaterialTheme.typography.bodyMedium
            )
            Button(
                onClick = {
                    isRecording = !isRecording
                    if (!isRecording && hasRecorded) {
                        recordingSavedMessage = true
                        hasRecorded = false
                        coroutineScope.launch {
                            delay(3000)
                            recordingSavedMessage = false
                        }
                    }
                },
                enabled = audioPermissionGranted,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = if (isRecording) "Stop Audio Recording" else "Start Audio Recording")
            }
        }

        // Decibel meter
        DecibelMeterSection(
            audioPermissionGranted = audioPermissionGranted,
            recording = isRecording,
            onAverageDecibelComputed = { average ->
                averageDecibel = average
                hasRecorded = true
            }
        )

        // Show message if recording is saved
        if (recordingSavedMessage) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Recording saved successfully!",
                    fontSize = 18.sp,
                    color = Color(0xFF2E7D32) // Dark green
                )
            }
        }

        // Confirm Button
        Button(
            onClick = {
                val result = validateSleepInput(
                    sleepTime = sleepDateTime,
                    duration = sleepDuration,
                    sleepComment = sleepComment,
                    consentChecked = consentChecked
                )
                if (!result.isValid) {
                    validationMessage = result.message
                    showValidationError = true
                } else {
                    showValidationError = false
                    coroutineScope.launch {
                        averageDecibel?.let {
                            handleConfirmClick(
                                context = context,
                                sleepTime = sleepDateTime,
                                duration = sleepDuration,
                                sleepComment = sleepComment,
                                hasGeoPermission = locationPermissionGranted,
                                averageDecibel = it
                            )
                            Toast.makeText(context, "Data added successfully", Toast.LENGTH_SHORT).show()
                            navController.navigate(Routes.HISTORY)
                        }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
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
