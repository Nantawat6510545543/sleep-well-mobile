package org.classapp.sleepwell.utils

import android.util.Log
import kotlin.math.log10
import kotlin.math.sqrt


fun computeDecibel(buffer: ShortArray, read: Int): Double {
    if (read <= 0) return 0.0
    val sumSquares = buffer.take(read).sumOf { (it * it).toDouble() }
    val rms = sqrt(sumSquares / read)
    val epsilon = 1e-6
    val decibel = if (rms > epsilon) 20 * log10(rms) else 0.0
//    Log.d("AudioUtils", "RMS: $rms, dB: $decibel")
    return decibel
}

