package com.example.fortressconquest.ui.screens.register

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fortressconquest.R
import com.example.fortressconquest.common.showToast
import com.example.fortressconquest.domain.utils.Response
import com.example.fortressconquest.ui.components.LoadingDialog
import com.example.fortressconquest.ui.components.OutlinedInputFieldWithError
import com.example.fortressconquest.ui.components.PasswordInputField
import com.example.fortressconquest.ui.screens.register.components.ImageSelectForm

@Composable
fun RegisterScreen(
    onNavigateToLoginScreen: () -> Unit,
    onRegisterSuccess: () -> Unit,
    onRegisterFailure: suspend (String) -> Unit,
    modifier: Modifier = Modifier,
    registerViewModel: RegisterViewModel = hiltViewModel(),
) {
    val formState by registerViewModel.registerFormState.collectAsStateWithLifecycle()
    val responseState by registerViewModel.registerResponseState.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val fieldWidth = dimensionResource(id = R.dimen.text_field_width)

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(vertical = dimensionResource(id = R.dimen.padding_large)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(
            dimensionResource(id = R.dimen.padding_small),
            Alignment.CenterVertically
        )
    ) {
        ImageSelectForm(
            imageUri = formState.imageUri,
            onImageTaken = registerViewModel::updateImageUri,
            onImageSelected = registerViewModel::updateImageUri,
            onCameraPermissionDenied = {
                showToast(context, R.string.error_permission_camera)
            }
        )

        OutlinedInputFieldWithError(
            field = formState.firstName,
            onValueChange = registerViewModel::updateFirstName,
            label = R.string.first_name,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.width(fieldWidth)
        )

        OutlinedInputFieldWithError(
            field = formState.lastName,
            onValueChange = registerViewModel::updateLastName,
            label = R.string.last_name,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.width(fieldWidth)
        )

        OutlinedInputFieldWithError(
            field = formState.email,
            onValueChange = registerViewModel::updateEmail,
            label = R.string.email,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.width(fieldWidth)
        )

        PasswordInputField(
            value = formState.password.value,
            onValueChange = registerViewModel::updatePassword,
            onTogglePasswordVisibility = registerViewModel::togglePasswordVisibility,
            isPasswordVisible = formState.isPasswordVisible,
            errorMessage = formState.password.error?.asString(),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            modifier = Modifier.width(fieldWidth)
        )

        Button(
            onClick = registerViewModel::submit,
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

    when (val response = responseState) {
        is Response.Success -> LaunchedEffect(responseState) {
            onRegisterSuccess()
        }
        is Response.Error -> LaunchedEffect(responseState) {
            onRegisterFailure(response.error.asString(context))
            registerViewModel.resetError()
        }
        is Response.Loading -> LoadingDialog()
        else -> Unit
    }
}