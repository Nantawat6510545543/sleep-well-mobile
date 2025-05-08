package org.classapp.sleepwell.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await
import org.classapp.sleepwell.utils.SleepLog


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
        Text(text = "History Details", fontSize = 32.sp, fontWeight = FontWeight.Bold)
        sleepLog?.let { log ->
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Sleep ID: $sleepId")
            Text(text = "Sleep Date: ${log.sleepDate.toDate()}")
            Text(text = "Duration: ${log.duration} minutes")
            Text(text = "Comment: ${log.sleepComment}")
            Text(text = "Sleep Score: ${log.sleepScore}")

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Place: ${log.place}")
            Text(text = "Temperature: ${log.tempC}°C")
            Text(text = "Humidity: ${log.humidity}%")
            Text(text = "Precipitation: ${log.precip}mm")
            Text(text = "Weather Condition: ${log.weatherCondition}")
            Text(text = "Noise Level: ${log.noise} dB")
        }

        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Back to History")
        }
    }
}


suspend fun getSleepLogById(sleepId: String): SleepLog? {
    val db = Firebase.firestore
    return try {
        val doc = db.collection("sleeps")
            .document(sleepId)
            .get()
            .await()
        doc.toObject(SleepLog::class.java)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
