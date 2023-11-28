package com.example.fortressconquest.ui.utils

sealed class GraphDestination(val route: String) {
    object Splash: GraphDestination("splash")
    object Auth: GraphDestination("auth")
    object Home: GraphDestination("home")
}