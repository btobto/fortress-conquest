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
import com.example.fortressconquest.ui.components.OutlinedInputFieldWithError
import com.example.fortressconquest.ui.components.PasswordInputField
import com.example.fortressconquest.ui.screens.register.components.ImageSelectForm

@Composable
fun RegisterFormScreen(
    onNavigateToLoginScreen: () -> Unit,
    onNextPage: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RegisterViewModel = hiltViewModel(),
) {
    // todo: top bar

    val formState by viewModel.registerFormState.collectAsStateWithLifecycle()

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
            onImageTaken = viewModel::updateImageUri,
            onImageSelected = viewModel::updateImageUri,
            onCameraPermissionDenied = {
                showToast(context, R.string.error_permission_camera)
            }
        )

        OutlinedInputFieldWithError(
            field = formState.firstName,
            onValueChange = viewModel::updateFirstName,
            label = R.string.first_name,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.width(fieldWidth)
        )

        OutlinedInputFieldWithError(
            field = formState.lastName,
            onValueChange = viewModel::updateLastName,
            label = R.string.last_name,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.width(fieldWidth)
        )

        OutlinedInputFieldWithError(
            field = formState.email,
            onValueChange = viewModel::updateEmail,
            label = R.string.email,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.width(fieldWidth)
        )

        PasswordInputField(
            value = formState.password.value,
            onValueChange = viewModel::updatePassword,
            onTogglePasswordVisibility = viewModel::togglePasswordVisibility,
            isPasswordVisible = formState.isPasswordVisible,
            errorMessage = formState.password.error?.asString(),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            modifier = Modifier.width(fieldWidth)
        )

        Button(
            onClick = onNextPage,
            enabled = viewModel.isFormValid()
        ) {
            Text(text = stringResource(id = R.string.next))
        }

        Text(
            text = stringResource(id = R.string.redirect_login_button),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .clickable(onClick = onNavigateToLoginScreen)
        )
    }
}