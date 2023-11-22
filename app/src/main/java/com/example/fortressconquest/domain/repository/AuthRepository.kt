package com.example.fortressconquest.domain.repository

import com.example.fortressconquest.domain.model.Response
import com.example.fortressconquest.domain.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow

interface AuthRepository {
    suspend fun register(email: String, password: String): Response<Boolean>
    suspend fun login(email: String, password: String): Response<Boolean>
    fun logout()
    fun getCurrentUser(scope: CoroutineScope): StateFlow<User?>
    fun isAuthenticated(scope: CoroutineScope): StateFlow<Boolean>
}