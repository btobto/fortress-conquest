package com.example.fortressconquest.domain.usecase

import com.example.fortressconquest.domain.model.AuthState
import com.example.fortressconquest.domain.model.User
import com.example.fortressconquest.domain.repository.AuthRepository
import com.example.fortressconquest.domain.repository.UsersRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class GetCurrentUserUseCase @Inject constructor(
    externalScope: CoroutineScope,
    authRepository: AuthRepository,
    private val usersRepository: UsersRepository
) {
    private val userFlow: StateFlow<AuthState<User>> = authRepository.authState
        .flatMapLatest { state -> when(state) {
                is AuthState.LoggedIn ->
                    flowOf(usersRepository.getUser(state.data.uid))
                        .map { user -> user!! }
                        .retry(5) { e ->
                            (e is NullPointerException).also { if (it) delay(1000) }
                        }
                        .flatMapLatest {
                            usersRepository
                                .getUserFlow(state.data.uid)
                                .map { user -> AuthState.LoggedIn(user!!)
                            }
                        }
                is AuthState.Loading -> flowOf(AuthState.Loading)
                is AuthState.NotLoggedIn -> flowOf(AuthState.NotLoggedIn)
            }
        }
        .stateIn(externalScope, SharingStarted.Eagerly, AuthState.Loading)

    operator fun invoke(): StateFlow<AuthState<User>> = userFlow
}