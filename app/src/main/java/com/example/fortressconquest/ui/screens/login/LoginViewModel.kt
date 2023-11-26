package com.example.fortressconquest.ui.screens.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fortressconquest.common.getErrorText
import com.example.fortressconquest.common.model.UiText
import com.example.fortressconquest.common.model.ValidationResult
import com.example.fortressconquest.common.validateEmail
import com.example.fortressconquest.common.validatePassword
import com.example.fortressconquest.domain.model.LoginData
import com.example.fortressconquest.domain.model.Response
import com.example.fortressconquest.domain.repository.AuthRepository
import com.example.fortressconquest.ui.utils.FormField
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginFormState(
    val email: FormField = FormField(),
    val password: FormField = FormField(),
    val isPasswordVisible: Boolean = false
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {
    private val _loginFormState = MutableStateFlow(LoginFormState())
    val loginFormState: StateFlow<LoginFormState> = _loginFormState.asStateFlow()

    private val _loginResponseState: MutableStateFlow<Response<Boolean>> = MutableStateFlow(Response.None)
    val loginResponseState: StateFlow<Response<Boolean>> = _loginResponseState.asStateFlow()

    fun updateEmail(input: String) {
        _loginFormState.update { currentState ->
            currentState.copy(
                email = FormField(
                    input,
                    getErrorText(validateEmail(input))
                )
            )
        }
    }

    fun updatePassword(input: String) {
        _loginFormState.update { currentState ->
            currentState.copy(
                password = FormField(
                    input,
                    getErrorText(validatePassword(input))
                )
            )
        }

    }

    fun togglePasswordVisibility() {
        _loginFormState.update { currentState ->
            currentState.copy(isPasswordVisible = !currentState.isPasswordVisible)
        }
    }

    fun submit() {
        viewModelScope.launch {
            _loginResponseState.update { Response.Loading }

            val response = loginFormState.value.run {
                authRepository.login(
                    LoginData(
                        email = email.value,
                        password = password.value
                    )
                )
            }

            _loginResponseState.update { response }
        }
    }

    fun resetError() {
        if (loginResponseState.value is Response.Error) {
            _loginResponseState.update { Response.None }
        }
    }

    fun isFormValid(): Boolean {
        return loginFormState.value.run {
            email.isValid() && password.isValid()
        }
    }
}