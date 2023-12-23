package com.example.fortressconquest.ui

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.fortressconquest.ui.navigation.AppNavHost
import com.example.fortressconquest.ui.theme.FortressConquestTheme

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