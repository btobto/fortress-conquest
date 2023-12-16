package com.example.fortressconquest.ui.navigation.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.fortressconquest.R
import com.example.fortressconquest.common.showToast
import com.example.fortressconquest.ui.navigation.GraphDestination
import com.example.fortressconquest.ui.navigation.navigateAndClearBackStack
import com.example.fortressconquest.ui.screens.character_select.CharacterSelectScreen
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