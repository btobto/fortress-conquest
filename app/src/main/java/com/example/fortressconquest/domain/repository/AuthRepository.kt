package com.example.fortressconquest.domain.repository

import com.example.fortressconquest.domain.model.AuthState
import com.example.fortressconquest.domain.model.RegistrationData
import com.example.fortressconquest.domain.model.LoginData
import com.example.fortressconquest.domain.model.Response
import com.example.fortressconquest.domain.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow

interface AuthRepository {
    suspend fun register(registrationData: RegistrationData): Response<Boolean>
    suspend fun login(loginData: LoginData): Response<Boolean>
    suspend fun reloadUser(): Response<Boolean>
    fun logout()
    fun getAuthState(scope: CoroutineScope): StateFlow<AuthState>
}