package com.example.fortressconquest.ui

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.fortressconquest.ui.components.BottomNavigationBar
import com.example.fortressconquest.ui.navigation.AppNavHost
import com.example.fortressconquest.ui.theme.FortressConquestTheme
import com.example.fortressconquest.ui.utils.BottomBarDestination
import com.example.fortressconquest.ui.utils.bottomBarDestinations

private const val TAG = "backstack"

@Composable
fun FortressConquestApp() {
    FortressConquestTheme {
        val navController = rememberNavController()
        val snackbarHostState = remember { SnackbarHostState() }

        navController.addOnDestinationChangedListener { controller, _, _ ->
            val backStack = controller.backQueue
                .map { it.destination.route }
                .joinToString(" -> ")

            Log.d(TAG, "backStack: $backStack")
        }

        Scaffold(
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            },
            bottomBar = {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                val shouldShowBottomNavBar = bottomBarDestinations.map { it.route }
                    .contains(currentDestination?.route)

                if (shouldShowBottomNavBar) {
                    BottomNavigationBar(
                        destinations = bottomBarDestinations,
                        isItemSelected = { item ->
                            currentDestination?.hierarchy?.any { it.route == item } == true
                        },
                        onItemClick = { route ->
                            navController.navigate(route) {
                                popUpTo(BottomBarDestination.Map.route) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        ) { innerPadding ->
            AppNavHost(
                navController = navController,
                onShowSnackbar = { message, action ->
                    snackbarHostState.showSnackbar(
                        message = message,
                        actionLabel = action
                    ) == SnackbarResult.ActionPerformed
                },
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}