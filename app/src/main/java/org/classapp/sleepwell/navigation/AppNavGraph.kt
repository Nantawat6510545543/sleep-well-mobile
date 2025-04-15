package org.classapp.sleepwell.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavGraph(startDestination: String = Routes.SIGN_IN) {
    val navController = rememberNavController()

    NavHost (navController = navController, startDestination = startDestination) {
        authNavGraph(navController) // Auth flow
        mainNavGraph(navController) // Main app flow
    }
}