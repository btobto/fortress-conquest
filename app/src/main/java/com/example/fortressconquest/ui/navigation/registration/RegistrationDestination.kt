package com.example.fortressconquest.ui.navigation.registration

sealed class RegistrationDestination(val route: String) {
    object RegisterForm: RegistrationDestination("register_form")
    object CharacterSelect: RegistrationDestination("character_select")
}