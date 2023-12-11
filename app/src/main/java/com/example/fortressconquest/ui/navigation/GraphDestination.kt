package com.example.fortressconquest.ui.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class GraphDestination(val route: String) {
    object Splash: GraphDestination("splash")
    object Auth: GraphDestination("auth")
    object Main: GraphDestination("main") {
        const val CHECK_CHARACTER_ARG_NAME = "checkCharacter"
        private const val CHECK_CHARACTER_ARG = "checkCharacter"
        private val routeWithoutSetArgs = "${route}?$CHECK_CHARACTER_ARG_NAME"

        val routeWithArgs = "$routeWithoutSetArgs={$CHECK_CHARACTER_ARG}"
        val arguments = listOf(navArgument(CHECK_CHARACTER_ARG) {
            type = NavType.BoolType
            defaultValue = false
        })

        fun getRouteWithArg(checkCharacter: Boolean): String {
            return "$routeWithoutSetArgs=$checkCharacter"
        }
    }
}