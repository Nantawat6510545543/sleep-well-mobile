package org.classapp.sleepwell.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import org.classapp.sleepwell.utils.SleepLog
import org.classapp.sleepwell.utils.getSleepLogById


@Composable
fun HistoryDetailsScreen(navController: NavController, sleepId: String?) {
    var sleepLog by remember { mutableStateOf<SleepLog?>(null) }

    if (sleepId == null) {
        Text(text = "Invalid Sleep ID", color = Color.Red)
        return
    }

    LaunchedEffect(sleepId) {
        sleepLog = getSleepLogById(sleepId)
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "History Details",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        sleepLog?.let { log ->
            Card(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Sleep ID: $sleepId", fontWeight = FontWeight.Bold)
                    Text(text = "Sleep Date/Time: ${log.sleepTime.toDate()}")
                    Text(text = "Duration: ${log.duration} minutes")
                    Text(text = "Comment: ${log.sleepComment}")
                    Text(text = "Sleep Score: ${log.sleepScore}")

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(text = "Place: ${log.place}")
                    Text(text = "Temperature: ${log.tempC}°C")
                    Text(text = "Humidity: ${log.humidity}%")
                    Text(text = "Precipitation: ${log.precip}mm")
                    Text(text = "Weather Condition: ${log.weatherCondition}")
                    Text(text = "Noise Level: %.2f dB".format(log.noise))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Back to History")
        }
    }
}

