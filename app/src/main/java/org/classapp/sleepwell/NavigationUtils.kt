package org.classapp.sleepwell

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
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
            icon = Icons.Outlined.Home,
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
            icon = Icons.Outlined.Person,
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
