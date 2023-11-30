package com.example.fortressconquest.ui.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fortressconquest.domain.model.AuthState
import com.example.fortressconquest.domain.model.Response
import com.example.fortressconquest.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {
    val authState = authRepository.isAuthenticated()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), AuthState.Loading)
}