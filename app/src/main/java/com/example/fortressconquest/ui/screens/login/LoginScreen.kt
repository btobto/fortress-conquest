package com.example.fortressconquest.ui.screens.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.fortressconquest.domain.model.Response
import com.example.fortressconquest.ui.components.LoadingDialog
import com.example.fortressconquest.ui.components.OutlinedInputFieldWithError
import com.example.fortressconquest.ui.components.PasswordInputField
import com.example.fortressconquest.ui.components.SplashAppLogo


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onNavigateToRegisterScreen: () -> Unit,
    onLoginSuccess: () -> Unit,
    onLoginFailure: (String) -> Unit,
    loginViewModel: LoginViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val formState by loginViewModel.loginFormState.collectAsStateWithLifecycle()
    val responseState by loginViewModel.loginResponseState.collectAsStateWithLifecycle()

    val fieldWidth = TextFieldDefaults.MinWidth

    Column(
        modifier = modifier
            .padding(vertical = dimensionResource(id = R.dimen.padding_large))
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(
            dimensionResource(id = R.dimen.padding_small),
            Alignment.CenterVertically
        )
    ) {
        SplashAppLogo(fraction = 0.4f)

        OutlinedInputFieldWithError(
            field = formState.email,
            onValueChange = loginViewModel::updateEmail,
            label = R.string.email,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.width(fieldWidth)
        )

        PasswordInputField(
            value = formState.password.value,
            onValueChange = loginViewModel::updatePassword,
            onTogglePasswordVisibility = loginViewModel::togglePasswordVisibility,
            isPasswordVisible = formState.isPasswordVisible,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            modifier = Modifier.width(fieldWidth)
        )

        Button(
            onClick = loginViewModel::submit,
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


    when (val response = responseState) {
        is Response.Success -> LaunchedEffect(responseState) {
            onLoginSuccess()
        }
        is Response.Error -> LaunchedEffect(responseState) {
            onLoginFailure(response.error)
            loginViewModel.resetError()
        }
        is Response.Loading -> LoadingDialog()
        else -> Unit
    }
}