package com.example.fortressconquest.domain.model

import kotlin.math.floor
import kotlin.math.pow

data class User(
    val id: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val photoUri: String = "",
    val character: CharacterClass? = null,
    val xp: Int = 0,
    val level: Int = 1,
    val fortressCount: Int = 0,
) {
    companion object {
        private const val BASE_XP = 100
        private const val MODIFIER = 1.5
    }

    fun nextLevelXp(): Int {
        return floor(BASE_XP * level.toDouble().pow(MODIFIER)).toInt()
    }

    fun canSetFortress(): Boolean {
        return fortressCount < level
    }
}