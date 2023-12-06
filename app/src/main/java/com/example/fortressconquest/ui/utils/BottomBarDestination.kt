package com.example.fortressconquest.ui.utils

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Leaderboard
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Person
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.fortressconquest.R
import com.example.fortressconquest.ui.navigation.MainDestination

sealed class BottomBarDestination(
    val route: String,
    @StringRes val labelId: Int,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    object Map: BottomBarDestination(
        route = MainDestination.Map.route,
        labelId = R.string.map,
        selectedIcon = Icons.Filled.Map,
        unselectedIcon = Icons.Outlined.Map
    )
    object Leaderboard: BottomBarDestination(
        route = MainDestination.Leaderboard.route,
        labelId = R.string.leaderboard,
        selectedIcon = Icons.Filled.Leaderboard,
        unselectedIcon = Icons.Outlined.Leaderboard
    )
    object Profile: BottomBarDestination(
        route = MainDestination.Profile.route,
        labelId = R.string.profile,
        selectedIcon = Icons.Filled.Person,
        unselectedIcon = Icons.Outlined.Person
    )
}

val bottomBarDestinations = listOf(
    BottomBarDestination.Map,
    BottomBarDestination.Leaderboard,
    BottomBarDestination.Profile
)

//val bottomBarDestinations: List<BottomBarDestination> = BottomBarDestination::class.sealedSubclasses
//    .map { clazz -> clazz.objectInstance!! }