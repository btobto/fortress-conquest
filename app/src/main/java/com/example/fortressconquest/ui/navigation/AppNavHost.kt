package com.example.fortressconquest.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
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
                    navController.navigateAndClearBackStack(GraphDestination.Main.route)
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

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(navController: NavHostController): T {
    val navGraphRoute = destination.parent?.route ?: return hiltViewModel()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return hiltViewModel(parentEntry)
}