package com.example.fortressconquest.domain.model

data class User(
    val uid: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val photoUri: String = "",
    val character: CharacterClass? = null
)