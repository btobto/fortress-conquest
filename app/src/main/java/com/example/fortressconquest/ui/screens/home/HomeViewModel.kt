package com.example.fortressconquest.ui.screens.home

import androidx.lifecycle.ViewModel
import com.example.fortressconquest.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {
    fun logout() {
        authRepository.logout()
    }
}