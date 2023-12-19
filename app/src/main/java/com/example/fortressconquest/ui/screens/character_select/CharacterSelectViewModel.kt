package com.example.fortressconquest.ui.screens.character_select

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fortressconquest.R
import com.example.fortressconquest.common.model.UiText
import com.example.fortressconquest.domain.model.AuthState
import com.example.fortressconquest.domain.model.CharacterClass
import com.example.fortressconquest.domain.model.Response
import com.example.fortressconquest.domain.repository.UsersRepository
import com.example.fortressconquest.domain.usecase.GetCurrentUserUseCase
import com.example.fortressconquest.ui.navigation.GraphDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "CharSelectVM"

@HiltViewModel
class CharacterSelectViewModel @Inject constructor(
    private val usersRepository: UsersRepository,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
): ViewModel() {

    val characterDialogState: Flow<Response<List<CharacterClass>, UiText>> =
        getCurrentUserUseCase()
            .map { state ->
                when (state) {
                    is AuthState.LoggedIn -> Response.Success(
                    state.data.character?.let { usersRepository.getAllCharacterClasses() } ?: emptyList()
                    )
                    is AuthState.Loading -> Response.Loading
                    else -> Response.Error(UiText.StringResource(resId = R.string.error_generic))
                }
            }

    fun selectCharacterClass(character: CharacterClass) {
        viewModelScope.launch {
            val state = getCurrentUserUseCase().value

            if (state is AuthState.LoggedIn) {
                usersRepository.setUserCharacterClass(state.data, character)
            }
        }
    }
}