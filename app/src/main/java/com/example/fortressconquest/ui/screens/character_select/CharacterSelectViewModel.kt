package com.example.fortressconquest.ui.screens.character_select

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fortressconquest.R
import com.example.fortressconquest.common.model.UiText
import com.example.fortressconquest.domain.model.AuthState
import com.example.fortressconquest.domain.model.CharacterClass
import com.example.fortressconquest.domain.model.Response
import com.example.fortressconquest.domain.repository.AuthRepository
import com.example.fortressconquest.domain.repository.UsersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharacterSelectViewModel @Inject constructor(
    private val usersRepository: UsersRepository,
    private val authRepository: AuthRepository
): ViewModel() {

    val characterDialogState: Flow<Response<List<CharacterClass>, UiText>> =
        authRepository.authState
            .map { state ->
                when (state) {
                    is AuthState.LoggedIn -> Response.Success(
                        if (state.data.character == null) {
                            usersRepository.getAllCharacterClasses()
                        } else {
                            emptyList()
                        }
                    )
                    is AuthState.Loading -> Response.Loading
                    else -> Response.Error(UiText.StringResource(resId = R.string.error_generic))
                }
            }


    fun selectCharacterClass(character: CharacterClass) {
        viewModelScope.launch {
            val state = authRepository.authState.value

            if (state is AuthState.LoggedIn) {
                usersRepository.setUserCharacterClass(state.data, character)
            }
        }
    }
}