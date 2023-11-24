package com.example.fortressconquest.ui.utils

sealed class Destination(val route: String) {
    object Splash: Destination("splash")
    object Home: Destination("home")
    object Login: Destination("login")
    object Register: Destination("register")
}