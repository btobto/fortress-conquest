package com.example.fortressconquest.ui.navigation.main

sealed class MainDestination(val route: String) {
    object Map: MainDestination("map")
    object Leaderboard: MainDestination("leaderboard")
    object Profile: MainDestination("profile")
}