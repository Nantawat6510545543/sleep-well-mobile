package org.classapp.sleepwell.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth

@Composable
fun HomeScreen() {
    val user = FirebaseAuth.getInstance().currentUser   // TODO update user state when logout

    // Title Text TODO refactor to component
    if (user != null) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
        ) {
            // Title Text
            Text(
                text = "Welcome, ${user.displayName}",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Text(user.uid)
        }
    }
}

