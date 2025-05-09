import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.core.chart.line.LineChart
import com.patrykandpatrick.vico.core.entry.FloatEntry
import com.patrykandpatrick.vico.core.entry.entryModelOf
import org.classapp.sleepwell.utils.SleepEntry
import java.text.SimpleDateFormat
import java.util.Locale


@Composable
fun SleepScoreChart(sleepEntries: List<SleepEntry>) {
    // Create entries for the chart
    val entries = sleepEntries.mapIndexed { index, item ->
        FloatEntry(index.toFloat(), item.sleepScore) // Mapping the score to FloatEntry
    }

    // Format the x-axis labels (dates)
    val xLabels = sleepEntries.map {
        SimpleDateFormat("MMM d", Locale.getDefault()).format(it.sleepTime.toDate())
    }

    val lineSpec = LineChart.LineSpec(
        lineColor = MaterialTheme.colorScheme.primary.toArgb(),
    )

    // Center text in box
    Box(modifier = Modifier.fillMaxWidth()) {
        // Title Text
        Text(
            text = "Sleep Score Over Time",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(top = 16.dp, bottom = 16.dp)
        )
    }
    // Chart
    Chart(
        chart = lineChart(
            lines = listOf(lineSpec)
        ),
        model = entryModelOf(entries),
        startAxis = rememberStartAxis(),
        bottomAxis = rememberBottomAxis(
            valueFormatter = { value, _ ->
                xLabels.getOrNull(value.toInt()) ?: ""
            }
        ),
    )
}
