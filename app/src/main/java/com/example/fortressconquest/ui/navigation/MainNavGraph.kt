package com.example.fortressconquest.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.fortressconquest.ui.screens.home.HomeScreen
import com.example.fortressconquest.ui.screens.leaderboard.LeaderboardScreen
import com.example.fortressconquest.ui.screens.profile.ProfileScreen
import com.example.fortressconquest.ui.utils.GraphDestination

sealed class MainDestination(val route: String) {
    object Map: MainDestination("map")
    object Leaderboard: MainDestination("leaderboard")
    object Profile: MainDestination("profile")
}

fun NavGraphBuilder.mainGraph(
    navController: NavHostController
) {
    navigation(
        route = GraphDestination.Main.route,
        startDestination = MainDestination.Map.route
    ) {
        composable(route = MainDestination.Map.route) {
            HomeScreen(

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