package org.classapp.sleepwell

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource

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
            route = AppScreen.Home.route
        ),
        NavigationItem(
            title = "History",
            icon = getHistoryIcon(),
            route = AppScreen.History.route
        ),
        NavigationItem(
            title = "Analytics",
            icon = getAnalyticsIcon(),
            route = AppScreen.Analytics.route
        ),
        NavigationItem(
            title = "Profile",
            icon = Icons.Outlined.Person,
            route = AppScreen.Profile.route
        )
    )
}

sealed class AppScreen(val route: String) {
    data object Home: AppScreen("home_screen")
    data object History: AppScreen("profile_screen")
    data object Analytics: AppScreen("cart_screen")
    data object Profile: AppScreen("setting_screen")
}
