package org.classapp.sleepwell.screens

import android.Manifest
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import org.classapp.sleepwell.navigations.Routes
import org.classapp.sleepwell.utils.*

@Composable
fun HomeScreen(navController: NavController) {
//    val context = LocalContext.current
    requestPermission(
        listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.RECORD_AUDIO
        )
    )

    val user = FirebaseAuth.getInstance().currentUser

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (user != null) {
            Text(
                text = "Welcome back,\n${user.displayName}!",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
        }

        // Latest Sleep Log Card
        Card(
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "Latest Sleep Log",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("You slept 7h last night.", fontSize = 16.sp)
                Text("Your sleep score: 95", fontSize = 16.sp)
                Text("Your sleep score improved by 5%!", fontSize = 16.sp)
            }
        }

        // Add Sleep Log Button
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Just have a sleep?", fontSize = 24.sp)
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

        SentimentSleepAnalysisScreen()
        Spacer(modifier = Modifier.weight(1f)) // push card to bottom
        // Quick Insight Card
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "Quick Insight:",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text("Your average sleep this week: 7.5hr", fontSize = 16.sp)
            }
        }
    }
}
