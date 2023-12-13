package com.example.fortressconquest.ui.navigation.auth

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.fortressconquest.ui.navigation.GraphDestination
import com.example.fortressconquest.ui.navigation.navigateAndClearBackStack
import com.example.fortressconquest.ui.screens.login.LoginScreen
import com.example.fortressconquest.ui.screens.register.RegisterScreen

fun NavGraphBuilder.authGraph(
    navController: NavHostController,
    onShowSnackbar: suspend (String, String?) -> Boolean,
) {
    navigation(
        route = GraphDestination.Auth.route,
        startDestination = AuthDestination.Login.route
    ) {
        composable(route = AuthDestination.Login.route) {
            LoginScreen(
                onNavigateToRegisterScreen = {
                    navController.navigate(AuthDestination.Register.route)
                },
                onLoginSuccess = {
                    navController.navigateAndClearBackStack(
                        GraphDestination.Main.getRouteWithArg(checkCharacter = true)
                    )
                },
                onLoginFailure = { error -> onShowSnackbar(error, null) }
            )
        }

        composable(route = AuthDestination.Register.route) {
            RegisterScreen(
                onNavigateToLoginScreen = {
                    navController.navigateAndClearBackStack(AuthDestination.Login.route)
                },
                onRegisterSuccess = {
                    navController.navigateAndClearBackStack(
                        GraphDestination.Main.getRouteWithArg(checkCharacter = true)
                    )
                },
                onRegisterFailure = { error -> onShowSnackbar(error, null) }
            )
        }
    }
}



