package com.example.fortressconquest.domain.repository

import com.example.fortressconquest.domain.model.CharacterClass
import com.example.fortressconquest.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UsersRepository {
    suspend fun getAllCharacterClasses(): List<CharacterClass>

    suspend fun getUser(uid: String): User?

    suspend fun createUser(user: User)

    suspend fun setUserCharacterClass(user: User, character: CharacterClass)

    fun getUserFlow(uid: String): Flow<User?>
}