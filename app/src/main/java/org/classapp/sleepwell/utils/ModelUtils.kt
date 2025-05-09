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

fun runSentimentModel(session: OrtSession, input: String): Float {
    val env = OrtEnvironment.getEnvironment()

    // Model expects tensor(string), so pass a string array
    val tensor = OnnxTensor.createTensor(env, arrayOf(arrayOf(input)))

    // Run the session to get the prediction
    val inputName = session.inputNames.first()
    val results = session.run(mapOf(inputName to tensor))

    // Extract the value from the first tensor
    val sentimentResult = results[0].value as Array<FloatArray>

    // Return the first element of the sentimentResult
    return sentimentResult[0][0]
}
