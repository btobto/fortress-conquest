package com.example.fortressconquest.domain.model

sealed interface AuthState {
    object Loading: AuthState
    data class LoggedIn(val user: User): AuthState
    object NotLoggedIn: AuthState
}