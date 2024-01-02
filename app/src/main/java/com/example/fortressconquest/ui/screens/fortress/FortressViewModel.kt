package com.example.fortressconquest.ui.screens.fortress

import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fortressconquest.R
import com.example.fortressconquest.common.model.UiText
import com.example.fortressconquest.domain.model.AuthState
import com.example.fortressconquest.domain.model.Response
import com.example.fortressconquest.domain.model.User
import com.example.fortressconquest.domain.repository.AuthRepository
import com.example.fortressconquest.domain.repository.LocationRepository
import com.example.fortressconquest.domain.repository.UsersRepository
import com.example.fortressconquest.domain.usecase.SimulateBattleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "FortressViewModel"

@HiltViewModel
class FortressViewModel @Inject constructor(
    locationRepository: LocationRepository,
    authRepository: AuthRepository,
    private val usersRepository: UsersRepository,
    private val simulateBattleUseCase: SimulateBattleUseCase
): ViewModel() {
    companion object {
        private const val BATTLE_DISTANCE_M = 10
    }

    val locationFlow = locationRepository.getCurrentLocationUpdates()
        .filterIsInstance<Response.Success<Location, String>>()
        .take(1)
    val currentUserState = authRepository.authState
        .filterIsInstance<AuthState.LoggedIn<User>>()
        .take(1)

    private val _fortressOwnerState: MutableStateFlow<Response<User, UiText>> = MutableStateFlow(Response.Loading)
    val fortressOwnerState = _fortressOwnerState.asStateFlow()

    fun getFortressOwner(id: String) {
        viewModelScope.launch {
            _fortressOwnerState.emit(Response.Loading)

            val user = usersRepository.getUser(id)
            if (user != null) {
                _fortressOwnerState.emit(Response.Success(user))
            } else {
                _fortressOwnerState.emit(Response.Error(
                    UiText.StringResource(R.string.error_user_not_found))
                )
            }
        }
    }

    fun canBattle(
        currentUserId: String,
        fortressOwnerId: String,
        userLocation: Location,
        fortressLocation: Location
    ): Boolean {
        return userLocation.distanceTo(fortressLocation) <= BATTLE_DISTANCE_M &&
            currentUserId != fortressOwnerId
    }

    fun battle(
        currentUser: User,
        fortressOwner: User,
        userLocation: Location,
        fortressLocation: Location
    ) {
        if (canBattle(
            currentUser.id,
            fortressOwner.id,
            userLocation,
            fortressLocation)
        ) {
            val winner = simulateBattleUseCase(currentUser, fortressOwner)
            Log.d(TAG, "Battle winner: ${winner.firstName} ${winner.lastName}")
        }
    }
}