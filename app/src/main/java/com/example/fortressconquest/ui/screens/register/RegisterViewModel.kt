package com.example.fortressconquest.ui.screens.register

import android.net.Uri
import android.util.Log
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import com.example.fortressconquest.R
import com.example.fortressconquest.common.getErrorText
import com.example.fortressconquest.common.model.UiText
import com.example.fortressconquest.common.model.ValidationResult
import com.example.fortressconquest.common.validateEmail
import com.example.fortressconquest.common.validatePassword
import com.example.fortressconquest.common.validatePhoneNumber
import com.example.fortressconquest.domain.repository.AuthRepository
import com.example.fortressconquest.ui.utils.FormField
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class RegisterFormState(
    val email: FormField = FormField(),
    val password: FormField = FormField(),
    val firstName: FormField = FormField(),
    val lastName: FormField = FormField(),
    val phoneNumber: FormField = FormField(),
    val imageUri: Uri = Uri.EMPTY,
    val isPasswordVisible: Boolean = false
)

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {
    private val _registerFormState = MutableStateFlow(RegisterFormState())
    val registerFormState = _registerFormState.asStateFlow()

    fun updateEmail(input: String) {
        _registerFormState.update { currentState ->
            currentState.copy(
                email = FormField(
                    input,
                    getErrorText(validateEmail(input))
                )
            )
        }
    }

    fun updatePassword(input: String) {
        _registerFormState.update { currentState ->
            currentState.copy(
                password = FormField(
                    input,
                    getErrorText(validatePassword(input))
                )
            )
        }
    }

    fun updateFirstName(input: String) {
        _registerFormState.update { currentState ->
            currentState.copy(
                firstName = FormField(
                    input,
                    getErrorText(
                        getEmptyStringValidationResult(
                            value = input,
                            errorId = R.string.error_first_name_empty
                        )
                    )
                )
            )
        }
    }

    fun updateLastName(input: String) {
        _registerFormState.update { currentState ->
            currentState.copy(
                lastName = FormField(
                    input,
                    getErrorText(
                        getEmptyStringValidationResult(
                            value = input,
                            errorId = R.string.error_last_name_empty
                        )
                    )
                )
            )
        }
    }

    fun updatePhoneNumber(input: String) {
        _registerFormState.update { currentState ->
            currentState.copy(
                phoneNumber = FormField(
                    input,
                    getErrorText(validatePhoneNumber(input))
                )
            )
        }
    }

    fun updateImageUri(uri: Uri) {
        Log.i("IMAGE", if (Uri.EMPTY.equals(uri)) "EMPTY URI" else uri.toString())

        _registerFormState.update { currentState ->
            currentState.copy(
                imageUri = uri
            )
        }
    }

    fun togglePasswordVisibility() {
        _registerFormState.update { currentState ->
            currentState.copy(isPasswordVisible = !currentState.isPasswordVisible)
        }
    }

    fun submit(
        onRegisterSuccess: () -> Unit,
        onRegisterFailure: (String) -> Unit
    ) {

    }

    fun isFormValid(): Boolean {
        return _registerFormState.value.run {
            email.isValid() &&
            password.isValid() &&
            firstName.isValid() &&
            lastName.isValid() &&
            phoneNumber.isValid() &&
            !Uri.EMPTY.equals(imageUri)
        }
    }

    private fun getEmptyStringValidationResult(
        value: String,
        @StringRes errorId: Int
    ): ValidationResult {
        return if (value.isBlank()) {
            ValidationResult.Error(
                UiText.StringResource(
                    resId = errorId
                )
            )
        } else {
            ValidationResult.Success
        }
    }
}