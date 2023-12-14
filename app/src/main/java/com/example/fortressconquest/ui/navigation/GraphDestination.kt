package com.example.fortressconquest.ui.navigation

sealed class GraphDestination(val route: String) {
    object Splash: GraphDestination("splash")
    object Auth: GraphDestination("auth")
    object CharacterSelect: GraphDestination("character_select")
    object Main: GraphDestination("main")
}