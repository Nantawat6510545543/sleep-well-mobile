import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HistoryDetailsScreen(sleepId: String?) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "History Details", fontSize = 32.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        if (sleepId != null) {
            Text(text = "Sleep ID: $sleepId")
        } else {
            Text(text = "Sleep ID not available.")
        }
//        Text(text = "Bedtime: ${sleepData.bedtime.toDate()}")
//        Text(text = "Comment: ${sleepData.comment}")
//        Text(text = "Duration: ${sleepData.duration} hours")
//        Text(text = "Sleep Score: ${sleepData.sleepScore}")
    }
}
