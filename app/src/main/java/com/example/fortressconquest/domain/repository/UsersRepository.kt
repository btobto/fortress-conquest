package com.example.fortressconquest.domain.repository

import androidx.paging.PagingData
import com.example.fortressconquest.domain.model.CharacterClass
import com.example.fortressconquest.domain.model.Fortress
import com.example.fortressconquest.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UsersRepository {
    suspend fun getAllCharacterClasses(): List<CharacterClass>

    suspend fun getUser(id: String): User?

    suspend fun getUsers(ids: List<String>): List<User>

    fun getUsersPaged(): Flow<PagingData<User>>

    suspend fun setUserCharacterClass(user: User, character: CharacterClass)

    suspend fun onBattleWin(winner: User, xp: Int, fortress: Fortress)
}