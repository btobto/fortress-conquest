package com.example.fortressconquest.domain.repository

import com.example.fortressconquest.domain.model.AuthResponse
import com.example.fortressconquest.domain.model.AuthState
import kotlinx.coroutines.flow.StateFlow

interface AuthRepository {
    val authState: StateFlow<AuthState<AuthResponse>>

    suspend fun register(email: String, password: String): AuthResponse

    suspend fun login(email: String, password: String): AuthResponse

    fun logout()
}