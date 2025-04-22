package org.classapp.sleepwell.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import org.classapp.sleepwell.R
import org.classapp.sleepwell.navigations.Routes

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
            route = Routes.HOME
        ),
        NavigationItem(
            title = "History",
            icon = getHistoryIcon(),
            route = Routes.HISTORY
        ),
        NavigationItem(
            title = "Analytics",
            icon = getAnalyticsIcon(),
            route = Routes.ANALYTICS
        ),
        NavigationItem(
            title = "Profile",
            icon = Icons.Outlined.Person,
            route = Routes.PROFILE
        )
    )
}
