package org.classapp.sleepwell.utils

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.lang.reflect.Type


// Function to process the input map, check for the weather condition, and update the map if necessary
fun encodeWeatherCondition(context: Context, condition: String): Int {
    // Note: To access, open Android Device Monitor:
    //
    // go to View -> Tool Windows -> Device File Explorer.
    // Navigate to the Internal Storage:
    // On the left panel, expand data -> data -> org.classapp.sleepwell.
    // Inside, you'll find the files directory, where context.filesDir files are stored.
    val file = File(context.filesDir, "weather_label_encoder_map.json")
    val gson = Gson()

    // Load the existing conditionTextMap or create a new one
    val conditionTextMap: MutableMap<String, Int> = if (file.exists()) {
        val json = file.readText()
        print(json)
        val mapType: Type = object : TypeToken<MutableMap<String, Int>>() {}.type
        gson.fromJson(json, mapType)
    } else {
        mutableMapOf()
    }

//    Reset value:
//    val conditionTextMap = mapOf(
//        "Clear" to 0,
//        "Thundery outbreaks possible" to 1,
//        "null" to 2,
//        "Light rain" to 3,
//        "Sunny" to 4,
//        "Light drizzle" to 5,
//        "Cloudy" to 6
//    )

    // Encode condition using getOrPut — if not found, assign a new ID
    val encodedCondition = conditionTextMap.getOrPut(condition) { conditionTextMap.size }

    // Save updated map to JSON file
    val updatedJson = gson.toJson(conditionTextMap)

    file.writeText(updatedJson)
    return encodedCondition
//    Debug Print
//    println("Encoded condition_text: $condition = $encodedCondition")
}