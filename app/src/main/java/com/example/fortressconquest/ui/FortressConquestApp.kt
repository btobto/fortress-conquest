package com.example.fortressconquest.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
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

@Composable
fun FortressConquestApp() {
    FortressConquestTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val navController = rememberNavController()

            NavHost(
                navController = navController,
                startDestination = Destination.Splash.route
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
                    HomeScreen()
                }

                composable(route = Destination.Login.route) {
                    LoginScreen(
                        onNavigateToRegisterScreen = {
                            navController.navigate(Destination.Register.route)
                        },
                        onUserLogin = {
                            navController.navigateAndClearBackStack(Destination.Home.route)
                        }
                    )
                }

                composable(route = Destination.Register.route) {
                    RegisterScreen(
                        onNavigateToLoginScreen = {
                            navController.navigateAndClearBackStack(Destination.Login.route)
                        },
                        onUserRegister = {
                            navController.navigateAndClearBackStack(Destination.Home.route)
                        }
                    )
                }
            }
        }
    }
}

fun NavHostController.navigateAndClearBackStack(route: String) {
    this.navigate(route) {
        popUpTo(this@navigateAndClearBackStack.backQueue.first().destination.id) {
            inclusive = true
        }
    }
}