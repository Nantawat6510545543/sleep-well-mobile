package org.classapp.sleepwell.screens

import SleepScoreChart
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import org.classapp.sleepwell.utils.SleepEntry
import org.classapp.sleepwell.utils.SleepLog
import org.classapp.sleepwell.utils.calculateAverageDuration
import org.classapp.sleepwell.utils.calculateAverageSleepScore
import org.classapp.sleepwell.utils.fetchSleepAnalyticsData
import org.classapp.sleepwell.utils.fetchSleepLog

@Composable
fun AnalyticsScreen() {
    val user = FirebaseAuth.getInstance().currentUser
    var sleepEntries by remember { mutableStateOf<List<SleepEntry>>(emptyList()) }
    var sleepLogs by remember { mutableStateOf<List<SleepLog>>(emptyList()) }

    LaunchedEffect(user) {
        user?.uid?.let { uid ->
            sleepEntries = fetchSleepAnalyticsData(uid)
            sleepLogs = fetchSleepLog(uid)
        }
    }

    // Calculate average sleep score and duration using the SleepEntry data
    val averageSleepScore = calculateAverageSleepScore(sleepLogs)
    val averageSleepDuration = calculateAverageDuration(sleepLogs)

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
    ) {
        // Title Text
        Text(
            text = "Visualization",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (sleepEntries.isNotEmpty()) {
            SleepScoreChart(sleepEntries)
        } else {
            Text("Loading...")
        }

        // Bottom Cards for Average Sleep Score and Duration
        Spacer(modifier = Modifier.weight(1f)) // Pushes the cards to the bottom

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Card for Average Sleep Score
            Card(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(0.25f),
                shape = androidx.compose.material3.MaterialTheme.shapes.small
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Average Sleep Score",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "%.2f".format(averageSleepScore),
                        fontSize = 24.sp,
                        color = Color.Black
                    )
                }
            }

            // Card for Average Sleep Duration
            Card(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(0.25f),
                shape = androidx.compose.material3.MaterialTheme.shapes.small
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Average Sleep Duration",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "%.2f hours".format(averageSleepDuration),
                        fontSize = 24.sp,
                        color = Color.Black
                    )
                }
            }
        }
    }
}