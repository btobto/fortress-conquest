package com.example.fortressconquest.ui.screens.splash

import androidx.lifecycle.ViewModel
import com.example.fortressconquest.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    authRepository: AuthRepository
): ViewModel() {
    val authState = authRepository.authState
}