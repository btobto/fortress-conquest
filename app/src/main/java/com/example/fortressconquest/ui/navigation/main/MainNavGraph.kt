package com.example.fortressconquest.ui.navigation.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.navigation
import com.example.fortressconquest.ui.navigation.GraphDestination
import com.example.fortressconquest.ui.navigation.navigateAndClearBackStack
import com.example.fortressconquest.ui.screens.fortress.FortressDialog
import com.example.fortressconquest.ui.screens.leaderboard.LeaderboardDialog
import com.example.fortressconquest.ui.screens.map.MapScreen
import com.example.fortressconquest.ui.screens.profile.ProfileDialog

fun NavGraphBuilder.mainGraph(
    navController: NavHostController
) {
    navigation(
        route = GraphDestination.Main.route,
        startDestination = MainDestination.Map.route
    ) {

        composable(route = MainDestination.Map.route) {
            MapScreen(
                modifier = Modifier.fillMaxSize(),
                onLogout = {
                    navController.navigateAndClearBackStack(GraphDestination.Auth.route)
                },
                onNavigateToProfile = { navController.navigate(MainDestination.Profile.route) },
                onNavigateToLeaderboard = { navController.navigate(MainDestination.Leaderboard.route) },
                onNavigateToFortress = { fortress ->
                    navController.navigate("${MainDestination.Fortress.route}/${fortress.id}")
                }
            )
        }

        dialog(route = MainDestination.Profile.route) {
            ProfileDialog(
                onLogout = {
                    navController.navigateAndClearBackStack(GraphDestination.Auth.route)
                }
            )
        }

        dialog(route = MainDestination.Leaderboard.route) {
            LeaderboardDialog()
        }

        dialog(route = MainDestination.Fortress.routeWithArgs) {
            FortressDialog()
        }
    }
}