package org.classapp.sleepwell.navigations

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import org.classapp.sleepwell.screens.MainScreen

fun NavGraphBuilder.mainNavGraph(navController: NavController) {
    composable(Routes.MAIN) {
        MainScreen(navController)
    }
}