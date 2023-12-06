package com.example.fortressconquest.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.fortressconquest.ui.screens.login.LoginScreen
import com.example.fortressconquest.ui.screens.register.RegisterScreen
import com.example.fortressconquest.ui.utils.GraphDestination

sealed class AuthDestination(val route: String) {
    object Login: AuthDestination("login")
    object Register: AuthDestination("register")
}

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
                    navController.navigateAndClearBackStack(GraphDestination.Main.route)
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
                    navController.navigateAndClearBackStack(GraphDestination.Main.route)
                },
                onRegisterFailure = { error -> onShowSnackbar(error, null) }
            )
        }
    }
}



