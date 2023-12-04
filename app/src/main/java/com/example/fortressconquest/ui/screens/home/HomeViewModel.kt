package com.example.fortressconquest.ui.screens.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fortressconquest.domain.model.AuthState
import com.example.fortressconquest.domain.model.CharacterClass
import com.example.fortressconquest.domain.model.Response
import com.example.fortressconquest.domain.repository.AuthRepository
import com.example.fortressconquest.domain.repository.UsersRepository
import com.example.fortressconquest.domain.usecase.GetCurrentUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val usersRepository: UsersRepository,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
): ViewModel() {
    val characterDialogState: StateFlow<Response<List<CharacterClass>?, String>> =
        getCurrentUserUseCase()
            .onEach { state ->
                val msg = when (state) {
                    is AuthState.LoggedIn -> "in"
                    is AuthState.NotLoggedIn -> "out"
                    is AuthState.Loading -> "loading"
                }
                Log.i("dialog", msg)
            }
            .map { state -> when (state) {
                    is AuthState.LoggedIn ->
                        if (state.data.character == null)
                            Response.Success(usersRepository.getAllCharacterClasses())
                        else {
                            Response.Success(null)
                        }
                    is AuthState.Loading -> Response.Loading
                    else -> Response.Success(null)
                }
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), Response.Loading)

    fun selectCharacterClass(character: CharacterClass) {
        viewModelScope.launch {
            val state = getCurrentUserUseCase().value

            if (state is AuthState.LoggedIn) {
                usersRepository.setUserCharacterClass(state.data, character)
            }
        }
    }

    fun logout() {
        authRepository.logout()
    }
}