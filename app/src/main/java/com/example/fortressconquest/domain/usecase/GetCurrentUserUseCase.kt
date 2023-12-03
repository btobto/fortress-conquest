package com.example.fortressconquest.domain.usecase

import com.example.fortressconquest.domain.model.AuthState
import com.example.fortressconquest.domain.model.User
import com.example.fortressconquest.domain.repository.AuthRepository
import com.example.fortressconquest.domain.repository.UsersRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class GetCurrentUserUseCase @Inject constructor(
    private val externalScope: CoroutineScope,
    private val authRepository: AuthRepository,
    private val usersRepository: UsersRepository
) {
    private val userFlow: StateFlow<AuthState<User>> = authRepository.authState
        .flatMapLatest { state -> when(state) {
                is AuthState.LoggedIn -> usersRepository
                    .getUserFlow(state.data.uid)
                    .map { user -> AuthState.LoggedIn(user!!) }
                is AuthState.Loading -> flowOf(AuthState.Loading)
                is AuthState.NotLoggedIn -> flowOf(AuthState.NotLoggedIn)
            }
        }.stateIn(externalScope, SharingStarted.Eagerly, AuthState.Loading)

    operator fun invoke(): StateFlow<AuthState<User>> = userFlow
}