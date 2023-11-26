package com.example.fortressconquest.ui.screens.splash

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fortressconquest.R
import com.example.fortressconquest.domain.model.AuthState
import com.example.fortressconquest.ui.components.SplashAppLogo

@Composable
fun SplashScreen(
    splashViewModel: SplashViewModel = hiltViewModel(),
    onNavigateToLoginScreen: () -> Unit,
    onNavigateToHomeScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    when (val state = splashViewModel.authState.collectAsStateWithLifecycle().value) {
        is AuthState.LoggedIn -> LaunchedEffect(state) {
            onNavigateToHomeScreen()
        }
        is AuthState.NotLoggedIn -> LaunchedEffect(state) {
            onNavigateToLoginScreen()
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