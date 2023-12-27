package com.example.fortressconquest.domain.repository

import com.example.fortressconquest.domain.model.CharacterClass
import com.example.fortressconquest.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UsersRepository {
    suspend fun getAllCharacterClasses(): List<CharacterClass>

    suspend fun getUser(id: String): User?

    suspend fun getUsers(ids: List<String>): List<User>

    suspend fun createUser(user: User)

    suspend fun setUserCharacterClass(user: User, character: CharacterClass)

    fun getUserFlow(id: String): Flow<User?>
}