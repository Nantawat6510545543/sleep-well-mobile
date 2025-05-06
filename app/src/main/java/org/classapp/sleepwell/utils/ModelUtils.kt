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

fun runModel(session: OrtSession, input: Any): Any {
    val env = OrtEnvironment.getEnvironment()

    // Convert input to tensor
    val tensor = when (input) {
        is String -> {
            // Model expects tensor(string), so pass a string array
            OnnxTensor.createTensor(env, arrayOf(arrayOf(input)))
        }
        is FloatArray -> {
            OnnxTensor.createTensor(env, input)
        }
        else -> throw IllegalArgumentException("Unsupported input type")
    }

    // Run the session to get the prediction
    val inputName = session.inputNames.first()
    val results = session.run(mapOf(inputName to tensor))

    // Return the result (assuming it's a tensor of floats)
    return results[0].value
}