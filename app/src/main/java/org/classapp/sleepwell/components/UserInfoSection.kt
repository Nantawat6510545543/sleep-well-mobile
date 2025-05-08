package org.classapp.sleepwell.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.*
import android.location.Location
import com.google.firebase.auth.FirebaseUser

@Composable
fun UserInfoSection(user: FirebaseUser) {
    Column(modifier = Modifier.padding(bottom = 16.dp)) {
        Text(
            text = "Welcome, ${user.displayName}",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Text(user.uid)
    }
}
