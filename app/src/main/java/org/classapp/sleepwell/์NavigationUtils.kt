package org.classapp.sleepwell

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.compose.composable
import androidx.navigation.createGraph

@Composable
fun getHistoryIcon(): ImageVector {
    return ImageVector.vectorResource(R.drawable.list_alt_24px)
}

@Composable
fun getAnalyticsIcon(): ImageVector {
    return ImageVector.vectorResource(R.drawable.analytics_24px)
}

data class NavigationItem(
    val title: String,
    val icon: ImageVector,
    val route: String
)

@Composable
fun getNavigationItems(): List<NavigationItem> {
    return listOf(
        NavigationItem(
            title = "Home",
            icon = Icons.Filled.Home,
            route = Screen.Home.route
        ),
        NavigationItem(
            title = "History",
            icon = getHistoryIcon(),
            route = Screen.History.route
        ),
        NavigationItem(
            title = "Analytics",
            icon = getAnalyticsIcon(),
            route = Screen.Analytics.route
        ),
        NavigationItem(
            title = "Profile",
            icon = Icons.Filled.Person,
            route = Screen.Profile.route
        )
    )
}

sealed class Screen(val route: String) {
    data object Home: Screen("home_screen")
    data object History: Screen("profile_screen")
    data object Analytics: Screen("cart_screen")
    data object Profile: Screen("setting_screen")
}

fun createNavGraph(navController: NavController): NavGraph {
    return navController.createGraph(startDestination = Screen.Home.route) {
        composable(route = Screen.Home.route) {
            HomeScreen()
        }
        composable(route = Screen.History.route) {
            HistoryScreen()
        }
        composable(route = Screen.Analytics.route) {
            AnalyticsScreen()
        }
        composable(route = Screen.Profile.route) {
            ProfileScreen()
        }
    }
}