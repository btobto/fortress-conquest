package com.example.fortressconquest.ui.screens.profile

import androidx.lifecycle.ViewModel
import com.example.fortressconquest.domain.utils.AuthState
import com.example.fortressconquest.domain.model.User
import com.example.fortressconquest.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.take
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {
    val currentUserState = authRepository.authState
        .filterIsInstance<AuthState.LoggedIn<User>>()
        .take(1)

    fun logout() {
        authRepository.logout()
    }
}