package com.example.fortressconquest.ui.navigation.registration

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.fortressconquest.ui.navigation.GraphDestination
import com.example.fortressconquest.ui.navigation.auth.AuthDestination
import com.example.fortressconquest.ui.navigation.navigateAndClearBackStack
import com.example.fortressconquest.ui.navigation.sharedViewModel
import com.example.fortressconquest.ui.screens.character_select.CharacterSelectScreen
import com.example.fortressconquest.ui.screens.register.RegisterFormScreen
import com.example.fortressconquest.ui.screens.register.RegisterViewModel

fun NavGraphBuilder.registrationGraph(
    navController: NavHostController,
    onShowSnackbar: suspend (String, String?) -> Boolean,
) {
    navigation(
        route = AuthDestination.Registration.route,
        startDestination = RegistrationDestination.RegisterForm.route
    ) {
        composable(route = RegistrationDestination.RegisterForm.route) {
            RegisterFormScreen(
                onNavigateToLoginScreen = {
                    navController.navigateAndClearBackStack(AuthDestination.Login.route)
                },
                onNextPage = {
                    navController.navigate(RegistrationDestination.CharacterSelect.route)
                },
                viewModel = it.sharedViewModel<RegisterViewModel>(navController)
            )
        }

        composable(route = RegistrationDestination.CharacterSelect.route) {
            CharacterSelectScreen(
                onRegisterSuccess = {
                    navController.navigateAndClearBackStack(GraphDestination.Main.route)
                },
                onRegisterFailure = { error -> onShowSnackbar(error, null) },
                viewModel = it.sharedViewModel<RegisterViewModel>(navController)
            )
        }
    }
}