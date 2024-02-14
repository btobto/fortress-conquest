package com.example.fortressconquest.domain.model

data class RegistrationData(
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val localPhotoUri: String?,
    val character: CharacterClass
)