package com.example.fortressconquest.ui.navigation.auth

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.fortressconquest.ui.navigation.GraphDestination
import com.example.fortressconquest.ui.navigation.navigateAndClearBackStack
import com.example.fortressconquest.ui.navigation.registration.registrationGraph
import com.example.fortressconquest.ui.screens.login.LoginScreen

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
                    navController.navigate(AuthDestination.Registration.route)
                },
                onLoginSuccess = {
                    navController.navigateAndClearBackStack(GraphDestination.Main.route)
                },
                onLoginFailure = { error -> onShowSnackbar(error, null) }
            )
        }

        registrationGraph(navController, onShowSnackbar)
    }
}



