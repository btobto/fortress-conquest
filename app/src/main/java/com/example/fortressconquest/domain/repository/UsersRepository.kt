package com.example.fortressconquest.domain.repository

import com.example.fortressconquest.domain.model.User

interface UsersRepository {
    suspend fun createUser(user: User)
}