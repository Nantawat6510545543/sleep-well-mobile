package org.classapp.sleepwell.utils

import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat


fun hasPermission(context: Context, permission: String): Boolean {
    return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
}

@Composable
fun requestPermission(
    permissions: List<String>
): Map<String, Boolean> {
    val context = LocalContext.current
    val permissionStates = remember { mutableStateMapOf<String, Boolean>() }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { results ->
        results.forEach { (permission, granted) ->
            permissionStates[permission] = granted
        }
    }

    LaunchedEffect(Unit) {
        val needsRequest = permissions.any { !hasPermission(context, it) }

        if (needsRequest) {
            permissionLauncher.launch(permissions.toTypedArray())
        } else {
            permissions.forEach {
                permissionStates[it] = true
            }
        }
    }

    return permissionStates
}