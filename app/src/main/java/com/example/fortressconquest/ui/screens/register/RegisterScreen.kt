package com.example.fortressconquest.ui.screens.register

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
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
fun RegisterScreen(
    onNavigateToLoginScreen: () -> Unit,
    onRegisterSuccess: () -> Unit,
    onRegisterFailure: (String) -> Unit,
    registerViewModel: RegisterViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val registerState by registerViewModel.registerFormState.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .padding(vertical = dimensionResource(id = R.dimen.padding_large))
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(
            dimensionResource(id = R.dimen.padding_medium),
            Alignment.CenterVertically
        )
    ) {
        SplashAppLogo(fraction = 0.4f)

        OutlinedInputFieldWithError(
            field = registerState.firstName,
            onValueChange = registerViewModel::updateFirstName,
            label = R.string.first_name,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            )
        )

        OutlinedInputFieldWithError(
            field = registerState.lastName,
            onValueChange = registerViewModel::updateLastName,
            label = R.string.last_name,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            )
        )

        OutlinedInputFieldWithError(
            field = registerState.phoneNumber,
            onValueChange = registerViewModel::updatePhoneNumber,
            label = R.string.phone_number,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Next
            )
        )

        OutlinedInputFieldWithError(
            field = registerState.email,
            onValueChange = registerViewModel::updateEmail,
            label = R.string.email,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            )
        )

        PasswordInputField(
            value = registerState.password.value,
            onValueChange = registerViewModel::updatePassword,
            onTogglePasswordVisibility = registerViewModel::togglePasswordVisibility,
            isPasswordVisible = registerState.isPasswordVisible,
            errorMessage = registerState.password.error?.asString(),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            )
        )

        Button(
            onClick = { registerViewModel.submit(onRegisterSuccess, onRegisterFailure) },
            enabled = registerViewModel.isFormValid()
        ) {
            Text(text = stringResource(id = R.string.register))
        }

        Text(
            text = stringResource(id = R.string.redirect_login_button),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .clickable(onClick = onNavigateToLoginScreen)
        )
    }
}