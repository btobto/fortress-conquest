package com.example.fortressconquest.domain.utils

sealed interface AuthState<out T> {
    object Loading: AuthState<Nothing>
    data class LoggedIn<out T>(val data: T): AuthState<T>
    object NotLoggedIn: AuthState<Nothing>
}