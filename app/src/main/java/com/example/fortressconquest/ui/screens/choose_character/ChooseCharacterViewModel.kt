package com.example.fortressconquest.ui.screens.choose_character

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fortressconquest.domain.model.AuthState
import com.example.fortressconquest.domain.model.CharacterClass
import com.example.fortressconquest.domain.model.Response
import com.example.fortressconquest.domain.repository.UsersRepository
import com.example.fortressconquest.domain.usecase.GetCurrentUserUseCase
import com.example.fortressconquest.ui.navigation.GraphDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChooseCharacterViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val usersRepository: UsersRepository,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
): ViewModel() {
    private val checkIfHasCharacter: Boolean =
        savedStateHandle[GraphDestination.Main.CHECK_CHARACTER_ARG_NAME] ?: false
    val characterDialogState: Flow<Response<List<CharacterClass>?, String>> =
        getCurrentUserUseCase()
            .map { state ->
                if (!checkIfHasCharacter) {
                    return@map Response.Success(emptyList())
                }

                when (state) {
                    is AuthState.LoggedIn ->
                        if (state.data.character == null)
                            Response.Success(usersRepository.getAllCharacterClasses())
                        else
                            Response.Success(emptyList())
                    is AuthState.Loading -> Response.Loading
                    else -> Response.Success(null)
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