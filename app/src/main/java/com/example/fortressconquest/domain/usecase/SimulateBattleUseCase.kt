package com.example.fortressconquest.domain.usecase

import android.util.Log
import com.example.fortressconquest.domain.model.BattleResult
import com.example.fortressconquest.domain.model.CharacterClass
import com.example.fortressconquest.domain.model.User
import javax.inject.Inject

private const val TAG = "SimulationUseCase"

class SimulateBattleUseCase @Inject constructor() {

    operator fun invoke(user1: User, user2: User): BattleResult {
        if (user1.character == null || user2.character == null) {
            throw IllegalArgumentException("Both users must have a character")
        }

        var health1 = user1.character.health
        var health2 = user2.character.health

        var round = 1

        var (attacker, defender) = if ((1..2).random() == 1) {
            user1.character to user2.character
        } else {
            user2.character to user1.character
        }

        while (health1 > 0 && health2 > 0) {
            if (shouldMiss(attacker.accuracy)) {
                continue
            }

            val damage = calculateDamage(attacker, defender)

            if (attacker == user1.character) {
                health2 -= damage
            } else {
                health1 -= damage
            }

            Log.d(TAG, "Round ${round++}, health1: $health1, health2: $health2, damage: $damage")

            attacker = defender.also { defender = attacker }
        }

        val (winner, loser) = if (health1 > 0) {
            user1 to user2
        } else {
            user2 to user1
        }

        return BattleResult(
            winner = winner,
            xpGained = calculateXpGained(winner, loser),
        )
    }

    private fun calculateDamage(attacker: CharacterClass, defender: CharacterClass): Int {
        val damage = if ((1..100).random() <= attacker.critChance) {
            (attacker.damage * 1.5).toInt()
        } else {
            attacker.damage
        }

        return if ((1..3).random() == 1) {
            damage
        } else {
            (damage * (1 - defender.armor / 100.0)).toInt()
        }
    }

    private fun shouldMiss(accuracy: Int): Boolean {
        return (1..100).random() > accuracy
    }

    private fun calculateXpGained(winner: User, loser: User): Int {
        val modifier = loser.level - winner.level
        return if (modifier > 0) {
            100 + modifier * 50
        } else {
            100
        }
    }
}