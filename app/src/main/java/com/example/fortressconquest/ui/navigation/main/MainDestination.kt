package com.example.fortressconquest.ui.navigation.main

sealed class MainDestination(val route: String) {
    object Map: MainDestination("map")
    object Leaderboard: MainDestination("leaderboard")
    object Profile: MainDestination("profile")
    object Fortress: MainDestination("fortress") {
        const val FORTRESS_ID_KEY = "fortressId"
        val routeWithArgs = "$route/{$FORTRESS_ID_KEY}"
    }
}