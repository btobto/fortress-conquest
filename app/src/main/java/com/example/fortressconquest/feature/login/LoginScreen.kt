package com.example.fortressconquest.feature.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fortressconquest.R
import com.example.fortressconquest.common.composables.EmailInputField
import com.example.fortressconquest.common.composables.PasswordInputField


@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel = hiltViewModel(),
    onNavigateToRegisterScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    val loginState by loginViewModel.loginState.collectAsStateWithLifecycle()

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(
            dimensionResource(id = R.dimen.padding_medium), 
            Alignment.CenterVertically
        )
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth(0.4f)
                .padding(vertical = dimensionResource(id = R.dimen.padding_medium))
        )
        Text(
            text = stringResource(id = R.string.app_name),
            style = MaterialTheme.typography.displaySmall
        )
        EmailInputField(
            value = loginState.email,
            onValueChange = loginViewModel::updateEmail,
            errorMessage = loginState.emailError?.asString(),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            )
        )
        PasswordInputField(
            value = loginState.password,
            onValueChange = loginViewModel::updatePassword,
            onTogglePasswordVisibility = loginViewModel::togglePasswordVisibility,
            isPasswordVisible = loginState.isPasswordVisible,
            errorMessage = loginState.passwordError?.asString(),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            )
        )
        Button(
            onClick = loginViewModel::submit,
            enabled = loginViewModel.validForm()
        ) {
            Text(text = stringResource(id = R.string.login))
        }
        Text(
            text = stringResource(id = R.string.register),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .clickable(onClick = onNavigateToRegisterScreen)
        )
    }
}