package com.example.fortressconquest.domain.repository

import com.example.fortressconquest.domain.model.AuthState
import com.example.fortressconquest.domain.model.RegistrationData
import com.example.fortressconquest.domain.model.User
import kotlinx.coroutines.flow.StateFlow

interface AuthRepository {
    val authState: StateFlow<AuthState<User>>

    suspend fun register(registrationData: RegistrationData)

    suspend fun login(email: String, password: String)

    fun logout()
}