package com.example.fortressconquest.ui.navigation.main

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.fortressconquest.ui.navigation.GraphDestination
import com.example.fortressconquest.ui.navigation.navigateAndClearBackStack
import com.example.fortressconquest.ui.screens.leaderboard.LeaderboardScreen
import com.example.fortressconquest.ui.screens.map.MapScreen
import com.example.fortressconquest.ui.screens.profile.ProfileScreen

fun NavGraphBuilder.mainGraph(
    navController: NavHostController,
    onShowSnackbar: suspend (String, String?) -> Boolean
) {
    navigation(
        route = GraphDestination.Main.route,
        startDestination = MainDestination.Map.route
    ) {

        composable(route = MainDestination.Map.route) {
            MapScreen(
                onLocationError = { error -> onShowSnackbar(error, null) }
            )
        }

        composable(route = MainDestination.Leaderboard.route) {
            LeaderboardScreen()
        }

        composable(route = MainDestination.Profile.route) {
            ProfileScreen(
                onLogout = {
                    navController.navigateAndClearBackStack(GraphDestination.Auth.route)
                }
            )
        }
    }
}