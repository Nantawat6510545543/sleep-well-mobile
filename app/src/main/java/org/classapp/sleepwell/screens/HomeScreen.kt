package org.classapp.sleepwell.screens

import android.Manifest
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import org.classapp.sleepwell.navigations.Routes
import org.classapp.sleepwell.utils.*

@Composable
fun HomeScreen(navController: NavController) {
    requestPermission(
        listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.RECORD_AUDIO,
        )
    )

    val user = FirebaseAuth.getInstance().currentUser

    val sleepLog by produceState<SleepLog?>(initialValue = null, user) {
        value = user?.uid?.let { fetchLatestSleepLog(it) }
    }

    val sleepData by produceState<List<SleepLog>>(initialValue = emptyList(), user) {
        value = user?.uid?.let { fetchSleepLog(it) } ?: emptyList()
    }

    val avgScore = remember(sleepData) { calculateAverageSleepScore(sleepData) }
    val avgDuration = remember(sleepData) { calculateAverageDuration(sleepData) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        user?.displayName?.let {
            Text(
                text = "Welcome back,\n$it!",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
        }

        Card(
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Latest Sleep Log", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, fontSize = 24.sp)
                Spacer(modifier = Modifier.height(8.dp))

                if (sleepLog != null) {
                    Text("You slept ${sleepLog!!.duration}h last night.", fontSize = 16.sp)
                    Text("Your sleep score: ${sleepLog!!.sleepScore}", fontSize = 16.sp)
                    Text("Comment: ${sleepLog!!.sleepComment}", fontSize = 16.sp)
                    Text("Weather: ${sleepLog!!.weatherCondition}, ${sleepLog!!.tempC}°C, Humidity: ${sleepLog!!.humidity}%", fontSize = 16.sp)
                } else {
                    Text("Loading sleep data...", fontSize = 16.sp)
                }
            }
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Just had a sleep?", fontSize = 24.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { navController.navigate(Routes.ADD_SLEEP_HISTORY) },
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                modifier = Modifier.fillMaxWidth(0.7f)
            ) {
                Text("Add Sleep Log", fontSize = 16.sp)
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Quick Insight:", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, fontSize = 24.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text("Your average sleep: ${"%.2f".format(avgDuration)} hr", fontSize = 16.sp)
                Text("Average sleep score: ${"%.2f".format(avgScore)}", fontSize = 16.sp)
            }
        }
    }
}
