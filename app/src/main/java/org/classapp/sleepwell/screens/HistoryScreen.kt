package org.classapp.sleepwell.screens

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import org.classapp.sleepwell.components.SleepListItem
import org.classapp.sleepwell.navigations.Routes

data class SleepData(
    val id: String,
    val sleepTime: Timestamp = Timestamp.now(),
    val sleepComment: String = "",
    val duration: Number = 0,
    val sleepScore: Number = 0
)

@Composable
fun HistoryScreen(navController: NavController) {
    val sleepList = remember { mutableStateListOf<SleepData>() }

    // Fetch data when the screen is launched
    LaunchedEffect(Unit) {
        val db = FirebaseFirestore.getInstance()
        val user = FirebaseAuth.getInstance().currentUser

        if (user != null) {
            db.collection("sleeps")
                .whereEqualTo("userId", user.uid)
                .orderBy("sleepTime", Query.Direction.DESCENDING) // Sort by most recent
                .get()
                .addOnSuccessListener { result ->
                    sleepList.clear()  // Clear current list before adding new data
                    for (document in result) {
                        val data = document.data
                        val id = document.id
                        val bedtime = data["sleepTime"] as? Timestamp ?: Timestamp.now()
                        val duration = (data["duration"] as? Number) ?: 0
                        val sleepScore = (data["sleepScore"] as? Number) ?: 0
                        val sleepComment = data["sleepComment"] as? String ?: ""

                        sleepList.add(
                            SleepData(
                                id = id,
                                sleepTime = bedtime,
                                duration = duration,
                                sleepScore = sleepScore,
                                sleepComment = sleepComment
                            )
                        )
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("HistoryScreen", "Error getting documents: ", exception)
                }
        } else {
            Log.d("HistoryScreen", "User is not authenticated.")
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize().padding(bottom = 80.dp)) {
            Text(
                text = "History Log",
                modifier = Modifier.padding(16.dp),
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
            )

            LazyColumn {
                items(sleepList) { sleep ->
                    SleepListItem(navController, sleep, onDelete = { sleepId ->
                        sleepList.removeAll { it.id == sleepId }
                    })
                }
            }
        }

        Button(
            onClick = {
                navController.navigate(Routes.ADD_SLEEP_HISTORY)
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        ) {
            Text("Add Sleep Log")
        }
    }
}
