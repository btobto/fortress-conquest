package com.example.fortressconquest.domain.usecase

import android.util.Log
import com.example.fortressconquest.domain.model.AuthState
import com.example.fortressconquest.domain.model.User
import com.example.fortressconquest.domain.repository.AuthRepository
import com.example.fortressconquest.domain.repository.UsersRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import kotlin.math.pow

private const val TAG = "CurrUserUC"

@OptIn(ExperimentalCoroutinesApi::class)
class GetCurrentUserUseCase @Inject constructor(
    externalScope: CoroutineScope,
    authRepository: AuthRepository,
    private val usersRepository: UsersRepository
) {
    private val userFlow = authRepository.authState
        .flatMapLatest { state ->
            when(state) {
                is AuthState.LoggedIn ->
                    usersRepository.getUserFlow(state.data.uid)
                        .map { user -> AuthState.LoggedIn(user!!) }
                        .retryWhen { _, attempt ->
                            if (attempt > 3) return@retryWhen false

                            val delayMs = 1000 * 2.0.pow(attempt.toInt()).toLong()
                            Log.i(TAG, "Retrying to get user, attempt: $attempt, delay: $delayMs")
                            delay(delayMs)

                            true
                        }
                        .onEach {
                            Log.i(TAG, "Get user success, user id: ${state.data.uid}")
                        }
                        .catch { e ->
                            Log.i(TAG, "Retries failed")
                            authRepository.logout()
                            throw e
                        }
                is AuthState.Loading -> flowOf(AuthState.Loading)
                is AuthState.NotLoggedIn -> flowOf(AuthState.NotLoggedIn)
            }
        }
        .stateIn(externalScope, SharingStarted.WhileSubscribed(), AuthState.Loading)

    operator fun invoke(): StateFlow<AuthState<User>> = userFlow
}