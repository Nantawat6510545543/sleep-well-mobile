package org.classapp.sleepwell.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import org.classapp.sleepwell.utils.rememberLocation

@Composable
fun HomeScreen() {
    val location = rememberLocation() // helper state management for geolocation
    val user = FirebaseAuth.getInstance().currentUser

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        if (user != null) {
            Text(
                text = "Welcome, ${user.displayName}",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Text(user.uid)

            location?.let {
                Text("Latitude: ${location.latitude}")
                Text("Longitude: ${location.longitude}")
            } ?: Text("Fetching location...")
        }
    }
}