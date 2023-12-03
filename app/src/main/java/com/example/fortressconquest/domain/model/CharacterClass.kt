package com.example.fortressconquest.domain.model

data class CharacterClass(
    val name: String = "",
    val damage: Int = -1,
    val armor: Int = -1,
    val health: Int = -1,
    val accuracy: Int = -1,
    val critChance: Int  = -1
)
