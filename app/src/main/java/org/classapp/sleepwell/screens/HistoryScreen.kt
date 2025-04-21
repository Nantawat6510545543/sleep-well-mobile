import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.classapp.sleepwell.navigation.Routes

data class SleepData(
    val id: String,
    val bedtime: Timestamp = Timestamp.now(),
    val comment: String = "",
    val duration: Number = 0,
    val sleepScore: Number = 0
)

@Composable
fun HistoryScreen(navController: NavController) {
    val sleepList = remember { mutableStateListOf<SleepData>() }

    LaunchedEffect(Unit) {
        val db = FirebaseFirestore.getInstance()
        val user = FirebaseAuth.getInstance().currentUser

        if (user != null) {
            db.collection("sleeps")
                .whereEqualTo("userId", user.uid)
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        val data = document.data
                        val id = document.id
                        val bedtime = data["bedtime"] as? Timestamp ?: Timestamp.now()
                        val duration = (data["duration"] as? Number) ?: 0
                        val sleepScore = (data["sleepScore"] as? Number) ?: 0
                        val comment = data["comment"] as? String ?: ""

                        sleepList.add(
                            SleepData(
                                id = id,
                                bedtime = bedtime,
                                duration = duration,
                                sleepScore = sleepScore,
                                comment = comment
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
                color = Color.Black
            )

            LazyColumn {
                items(sleepList) { sleep ->
                    SleepListItem(navController, sleep)
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

@Composable
fun SleepListItem(navController: NavController, sleep: SleepData) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(width = 1.dp, color = Color.Black, shape = RoundedCornerShape(12.dp))
            .background(Color.White, shape = RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "id: ${sleep.id}")
            Text(text = "Bedtime: ${sleep.bedtime.toDate()}")
            Text(text = "Comment: ${sleep.comment}")
            Text(text = "Duration: ${sleep.duration} hours")
            Text(text = "Sleep Score: ${sleep.sleepScore}")
            Spacer(modifier = Modifier.padding(top = 8.dp))
            Button(
                onClick = {
                    navController.navigate("${Routes.HISTORY_DETAILS}/${sleep.id}")
                },
                modifier = Modifier
                    .defaultMinSize(minWidth = 1.dp)
                    .wrapContentWidth()
            ) {
                Text("See More")
            }
        }
    }
}
