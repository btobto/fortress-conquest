package com.example.fortressconquest.ui.navigation.auth

sealed class AuthDestination(val route: String) {
    object Login: AuthDestination("login")
    object Register: AuthDestination("register")
}