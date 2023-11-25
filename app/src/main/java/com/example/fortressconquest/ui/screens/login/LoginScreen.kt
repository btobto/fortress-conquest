package com.example.fortressconquest.ui.screens.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fortressconquest.R
import com.example.fortressconquest.ui.components.OutlinedInputFieldWithError
import com.example.fortressconquest.ui.components.PasswordInputField
import com.example.fortressconquest.ui.components.SplashAppLogo


@Composable
fun LoginScreen(
    onNavigateToRegisterScreen: () -> Unit,
    onLoginSuccess: () -> Unit,
    onLoginFailure: (String) -> Unit,
    loginViewModel: LoginViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val loginState by loginViewModel.loginFormState.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .padding(vertical = dimensionResource(id = R.dimen.padding_large))
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(
            dimensionResource(id = R.dimen.padding_medium), 
            Alignment.CenterVertically
        )
    ) {
        SplashAppLogo(fraction = 0.4f)

        OutlinedInputFieldWithError(
            field = loginState.email,
            onValueChange = loginViewModel::updateEmail,
            label = R.string.email,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            )
        )

        PasswordInputField(
            value = loginState.password.value,
            onValueChange = loginViewModel::updatePassword,
            onTogglePasswordVisibility = loginViewModel::togglePasswordVisibility,
            isPasswordVisible = loginState.isPasswordVisible,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            )
        )

        Button(
            onClick = { loginViewModel.submit(onLoginSuccess, onLoginFailure) },
            enabled = loginViewModel.isFormValid()
        ) {
            Text(text = stringResource(id = R.string.login))
        }

        Text(
            text = stringResource(id = R.string.redirect_register_button),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .clickable(onClick = onNavigateToRegisterScreen)
        )
    }
}