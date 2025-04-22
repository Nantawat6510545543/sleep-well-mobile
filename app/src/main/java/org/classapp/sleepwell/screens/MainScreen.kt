package org.classapp.sleepwell.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import org.classapp.sleepwell.BottomNavigationBar
import org.classapp.sleepwell.navigations.Routes

// Note: navController for Auth, innerNavController for Main
@Composable
fun MainScreen(navController: NavController) {
    val innerNavController = rememberNavController()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = { BottomNavigationBar(innerNavController) }
    ) { innerPadding ->
        NavHost(
            navController = innerNavController,
            startDestination = Routes.HOME,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Routes.HOME) { HomeScreen() }
            composable(Routes.HISTORY) { HistoryScreen(innerNavController) }
            composable(Routes.ANALYTICS) { AnalyticsScreen() }
            composable(Routes.PROFILE) { ProfileScreen(navController) }
            composable(
                route = "${Routes.HISTORY_DETAILS}/{sleepId}",
                arguments = listOf(navArgument("sleepId") { type = NavType.StringType })
            ) { backStackEntry ->
                // Get the sleepId argument from the backStackEntry
                val sleepId = backStackEntry.arguments?.getString("sleepId")
                HistoryDetailsScreen(sleepId = sleepId)  // Pass sleepId to the composable
            }
            composable(Routes.ADD_SLEEP_HISTORY) { AddSleepHistoryScreen() }
        }
    }
}