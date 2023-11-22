package com.example.fortressconquest.ui.screens.login

import androidx.lifecycle.ViewModel
import com.example.fortressconquest.common.model.UiText
import com.example.fortressconquest.common.model.ValidationResult
import com.example.fortressconquest.common.validateEmail
import com.example.fortressconquest.common.validatePassword
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class LoginState(
    val email: String = "",
    val emailError: UiText? = null,
    val password: String = "",
    val passwordError: UiText? = null,
    val isPasswordVisible: Boolean = false
)

@HiltViewModel
class LoginViewModel @Inject constructor(): ViewModel() {
    private val _loginState = MutableStateFlow(LoginState())
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    fun updateEmail(input: String) {
        _loginState.update { currentState ->
            currentState.copy(
                email = input,
                emailError = getErrorText(validateEmail(input))
            )
        }
    }

    fun updatePassword(input: String) {
        _loginState.update { currentState ->
            currentState.copy(
                password = input,
                passwordError = getErrorText(validatePassword(input))
            )
        }

    }

    fun togglePasswordVisibility() {
        _loginState.update { currentState ->
            currentState.copy(isPasswordVisible = !currentState.isPasswordVisible)
        }
    }

    fun submit() {

    }

    fun validForm(): Boolean {
        return loginState.value.run {
            emailError == null &&
            passwordError == null &&
            email.isNotBlank() &&
            password.isNotBlank()
        }
    }

    private fun getErrorText(validationResult: ValidationResult): UiText? {
        return when (validationResult) {
            is ValidationResult.Error -> validationResult.error
            is ValidationResult.Success -> null
        }
    }
}