package org.classapp.sleepwell.utils

import ai.onnxruntime.OnnxTensor
import ai.onnxruntime.OrtEnvironment
import ai.onnxruntime.OrtSession
import android.content.Context


fun loadModel(context: Context, modelPath: String): OrtSession {
    val environment = OrtEnvironment.getEnvironment()
    val inputStream = context.assets.open(modelPath)
    val modelBytes = inputStream.readBytes()
    inputStream.close()
    return environment.createSession(modelBytes)
}

fun predictSentiment(context: Context, input: String): Double {
    val sentimentModelSession = loadModel(context, "sentiment_analysis_model.onnx")
    val env = OrtEnvironment.getEnvironment()

    // Model expects tensor(string), so pass a string array
    val tensor = OnnxTensor.createTensor(env, arrayOf(input))

    // Run inference on the model
    val inputName = sentimentModelSession.inputNames.first()  // Get the input name
    val result = sentimentModelSession.run(mapOf(inputName to tensor))

    // Assuming the second output contains the class probabilities (scale between 0 and 1)
    val prob = (result[1].value as Array<FloatArray>)[0][1]  // Class 1 probability
    val sentimentScore = prob * 2 - 1  // Scale [0, 1] -> [-1, 1]

    return sentimentScore.toDouble()
}

// TODO: compute meaningful score
fun predictSleepScore(context: Context, feature: SleepFeature): Double {
    val sleepModelSession = loadModel(context, "sleep_model.onnx")
    val env = OrtEnvironment.getEnvironment()
    return 123.4
}