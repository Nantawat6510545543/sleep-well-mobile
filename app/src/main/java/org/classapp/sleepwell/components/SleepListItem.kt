package org.classapp.sleepwell.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.classapp.sleepwell.navigations.Routes
import org.classapp.sleepwell.screens.SleepData
import org.classapp.sleepwell.utils.deleteSleepLog

@Composable
fun SleepListItem(navController: NavController, sleep: SleepData, onDelete: (String) -> Unit) {
    var showDeleteConfirmation by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Sleep ID: ${sleep.id}")
            Text(text = "Bedtime: ${sleep.sleepTime.toDate()}")
            Text(text = "Comment: ${sleep.sleepComment}")
            Text(text = "Duration: ${sleep.duration} hours")
            Text(text = "Sleep Score: ${sleep.sleepScore}")

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = {
                        navController.navigate("${Routes.HISTORY_DETAILS}/${sleep.id}")
                    },
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text("See More")
                }

                Button(
                    onClick = {
                        showDeleteConfirmation = true
                    }
                ) {
                    Text("Delete")
                }
            }
        }
    }

    if (showDeleteConfirmation) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmation = false },
            title = { Text("Are you sure?") },
            text = { Text("Do you really want to delete this sleep log? This action cannot be undone.") },
            confirmButton = {
                TextButton(onClick = {
                    deleteSleepLog(sleep.id)
                    onDelete(sleep.id) // Update the UI by removing the item
                    showDeleteConfirmation = false
                }) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmation = false }) {
                    Text("No")
                }
            }
        )
    }
}
