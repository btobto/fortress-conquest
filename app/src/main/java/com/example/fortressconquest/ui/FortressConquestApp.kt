package com.example.fortressconquest.ui

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.fortressconquest.ui.components.BottomNavigationBar
import com.example.fortressconquest.ui.navigation.AppNavHost
import com.example.fortressconquest.ui.utils.bottomBarDestinations
import com.example.fortressconquest.ui.theme.FortressConquestTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FortressConquestApp() {
    FortressConquestTheme {
        val navController = rememberNavController()
        val snackbarHostState = remember { SnackbarHostState() }

        Scaffold(
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            },
            bottomBar = {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                val shouldShowBottomNavBar = bottomBarDestinations.map { it.route }
                    .contains(currentRoute)

                if (shouldShowBottomNavBar) {
                    BottomNavigationBar(
                        destinations = bottomBarDestinations,
                        onItemClick = { route ->
                            navController.navigate(route) {
                                popUpTo(navController.graph.findStartDestination().id) {
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