package com.example.fortressconquest.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.fortressconquest.ui.navigation.auth.authGraph
import com.example.fortressconquest.ui.navigation.main.MainDestination
import com.example.fortressconquest.ui.navigation.main.mainGraph
import com.example.fortressconquest.ui.screens.character_select.CharacterSelectScreen
import com.example.fortressconquest.ui.screens.splash.SplashScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = GraphDestination.Splash.route,
        modifier = modifier
    ) {
        composable(route = GraphDestination.Splash.route) {
            SplashScreen(
                onNavigateToAuth = {
                    navController.navigateAndClearBackStack(GraphDestination.Auth.route)
                },
                onNavigateToApp = {
                    navController.navigateAndClearBackStack(
                        GraphDestination.CharacterSelect.route
                    )
                }
            )
        }

        authGraph(navController, onShowSnackbar)

        composable(route = GraphDestination.CharacterSelect.route) {
            CharacterSelectScreen(
                onNavigateToApp = {
                    navController.navigateAndClearBackStack(
                        MainDestination.Map.route
                    )
                }
            )
        }

        mainGraph(navController)
    }
}

fun NavHostController.navigateAndClearBackStack(route: String) {
    this.navigate(route) {
        popUpTo(this@navigateAndClearBackStack.graph.id) {
            inclusive = true
        }
    }
}