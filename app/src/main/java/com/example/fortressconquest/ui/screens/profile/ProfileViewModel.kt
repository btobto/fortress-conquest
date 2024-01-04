package com.example.fortressconquest.ui.screens.profile

import androidx.lifecycle.ViewModel
import com.example.fortressconquest.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {

}