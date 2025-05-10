package org.classapp.sleepwell.screens

import android.util.Log
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import org.classapp.sleepwell.utils.loadModel
import org.classapp.sleepwell.utils.runSentimentModel

@Composable
fun SentimentSleepAnalysisScreen() {
    var inputText by remember { mutableStateOf("") }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // Create sessions for models
    val sentimentModelSession = remember { loadModel(context, "sentiment_analysis_model.onnx") }
    val sleepModelSession = remember { loadModel(context, "sleep_model.onnx") }

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = inputText,
            onValueChange = { inputText = it },
            label = { Text("Enter text") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            scope.launch {
                // Run Sentiment Analysis using the helper function
                val sentimentScore = runSentimentModel(sentimentModelSession, inputText)

                // Log the Sentiment result
                Log.d("SentimentAnalysis", "Sentiment Result: $sentimentScore")

//                // Use the sentiment result for sleep prediction
//                val sleepPrediction = runModel(sleepModelSession, sentimentResult) as FloatArray
//
//                // Log the Sleep prediction result
//                Log.d("SleepPrediction", "Predicted Sleep: ${sleepPrediction[0]}")
            }
        }) {
            Text("Analyze")
        }
    }
}
