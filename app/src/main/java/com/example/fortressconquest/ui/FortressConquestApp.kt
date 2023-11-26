package com.example.fortressconquest.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fortressconquest.ui.screens.home.HomeScreen
import com.example.fortressconquest.ui.screens.login.LoginScreen
import com.example.fortressconquest.ui.screens.register.RegisterScreen
import com.example.fortressconquest.ui.screens.splash.SplashScreen
import com.example.fortressconquest.ui.theme.FortressConquestTheme
import com.example.fortressconquest.ui.utils.Destination
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FortressConquestApp() {
    FortressConquestTheme {
        val navController = rememberNavController()

        val scope = rememberCoroutineScope()
        val snackbarHostState = remember { SnackbarHostState() }

        Scaffold(
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Destination.Splash.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(route = Destination.Splash.route) {
                    SplashScreen(
                        onNavigateToLoginScreen = {
                            navController.navigateAndClearBackStack(Destination.Login.route)
                        },
                        onNavigateToHomeScreen = {
                            navController.navigateAndClearBackStack(Destination.Home.route)
                        }
                    )
                }

                composable(route = Destination.Home.route) {
                    HomeScreen(
                        onLogout = {
                            navController.navigateAndClearBackStack(Destination.Login.route)
                        }
                    )
                }

                composable(route = Destination.Login.route) {
                    LoginScreen(
                        onNavigateToRegisterScreen = {
                            navController.navigate(Destination.Register.route)
                        },
                        onLoginSuccess = {
                            navController.navigateAndClearBackStack(Destination.Home.route)
                        },
                        onLoginFailure = { error ->
                            scope.launch {
                                snackbarHostState.showSnackbar(message = error)
                            }
                        }
                    )
                }

                composable(route = Destination.Register.route) {
                    RegisterScreen(
                        onNavigateToLoginScreen = {
                            navController.navigateAndClearBackStack(Destination.Login.route)
                        },
                        onRegisterSuccess = {
                            navController.navigateAndClearBackStack(Destination.Home.route)
                        },
                        onRegisterFailure = { error ->
                            scope.launch {
                                snackbarHostState.showSnackbar(message = error)
                            }
                        }
                    )
                }
            }
        }

    }
}

fun NavHostController.navigateAndClearBackStack(route: String) {
    this.navigate(route) {
        popUpTo(this@navigateAndClearBackStack.graph.id) {
            inclusive = true
        }
    }
}