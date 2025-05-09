package org.classapp.sleepwell.screens

import SleepScoreChart
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import org.classapp.sleepwell.utils.fetchSleepAnalyticsData

@Composable
fun AnalyticsScreen() {
    val user = FirebaseAuth.getInstance().currentUser
    var sleepEntries by remember { mutableStateOf<List<SleepEntry>>(emptyList()) }

    LaunchedEffect(Unit) {
        if (user != null) {
            fetchSleepAnalyticsData(user.uid) { entries ->
                sleepEntries = entries
            }
        }
    }

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
    }
}
