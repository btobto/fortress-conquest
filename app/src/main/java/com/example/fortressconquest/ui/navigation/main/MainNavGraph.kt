package com.example.fortressconquest.ui.navigation.main

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.fortressconquest.ui.navigation.navigateAndClearBackStack
import com.example.fortressconquest.ui.screens.home.HomeScreen
import com.example.fortressconquest.ui.screens.leaderboard.LeaderboardScreen
import com.example.fortressconquest.ui.screens.profile.ProfileScreen
import com.example.fortressconquest.ui.navigation.GraphDestination
import com.example.fortressconquest.ui.screens.choose_character.ChooseCharacterScreen

fun NavGraphBuilder.mainGraph(
    navController: NavHostController
) {
    navigation(
        route = GraphDestination.Main.routeWithArgs,
        startDestination = MainDestination.ChooseCharacter.route,
        arguments = GraphDestination.Main.arguments
    ) {
        composable(route = MainDestination.ChooseCharacter.route) {
            ChooseCharacterScreen(
                onSelectedCharacter = {
                    navController.navigate(MainDestination.Map.route) {
                        popUpTo(navController.graph.findStartDestination().id)
                    }
                }
            )
        }

        composable(route = MainDestination.Map.route) {
            HomeScreen()
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