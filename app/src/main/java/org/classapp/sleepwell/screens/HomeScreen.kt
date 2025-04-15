package org.classapp.sleepwell.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth

@Composable
fun HomeScreen() {
    val user = FirebaseAuth.getInstance().currentUser   // TODO update user state when logout

    if (user != null) {
        Column {
            Text(text = "Welcome, UID: ${user.uid}")
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
