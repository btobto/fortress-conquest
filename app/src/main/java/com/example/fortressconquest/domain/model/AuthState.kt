package com.example.fortressconquest.domain.model

sealed interface AuthState {
    object Loading: AuthState
    object LoggedIn: AuthState
    object NotLoggedIn: AuthState
}