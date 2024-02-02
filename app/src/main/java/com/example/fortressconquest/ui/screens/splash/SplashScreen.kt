package com.example.fortressconquest.ui.screens.splash

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fortressconquest.R
import com.example.fortressconquest.domain.utils.AuthState
import com.example.fortressconquest.ui.components.SplashAppLogo

@Composable
fun SplashScreen(
    onNavigateToAuth: () -> Unit,
    onNavigateToApp: () -> Unit,
    modifier: Modifier = Modifier,
    splashViewModel: SplashViewModel = hiltViewModel()
) {
    when (val state = splashViewModel.authState.collectAsStateWithLifecycle(AuthState.Loading).value) {
        is AuthState.LoggedIn -> LaunchedEffect(state) {
            onNavigateToApp()
        }
        is AuthState.NotLoggedIn -> LaunchedEffect(state) {
            onNavigateToAuth()
        }
        is AuthState.Loading -> SplashScreenContent(modifier.fillMaxSize())
    }
}

@Composable
fun SplashScreenContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        SplashAppLogo(fraction = 0.4f)
        Spacer(modifier = Modifier.size(dimensionResource(id = R.dimen.padding_large)))
        CircularProgressIndicator(
            Modifier.width(64.dp),
            color = MaterialTheme.colorScheme.secondary
        )
    }
}