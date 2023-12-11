package com.example.fortressconquest.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.fortressconquest.ui.navigation.auth.authGraph
import com.example.fortressconquest.ui.navigation.main.mainGraph
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
                        GraphDestination.Main.getRouteWithArg(checkCharacter = true)
                    )
                }
            )
        }

        authGraph(navController, onShowSnackbar)

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