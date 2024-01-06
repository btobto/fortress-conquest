package com.example.fortressconquest.ui.screens.fortress

import android.location.Location
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fortressconquest.R
import com.example.fortressconquest.common.model.UiText
import com.example.fortressconquest.domain.model.AuthState
import com.example.fortressconquest.domain.model.BattleResult
import com.example.fortressconquest.domain.model.Fortress
import com.example.fortressconquest.domain.model.Response
import com.example.fortressconquest.domain.model.User
import com.example.fortressconquest.domain.repository.AuthRepository
import com.example.fortressconquest.domain.repository.FortressesRepository
import com.example.fortressconquest.domain.repository.LocationRepository
import com.example.fortressconquest.domain.repository.UsersRepository
import com.example.fortressconquest.domain.usecase.SimulateBattleUseCase
import com.example.fortressconquest.ui.navigation.main.MainDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "FortressViewModel"

sealed interface BattleState {
    object NotStarted: BattleState
    object InProgress: BattleState
    data class Finished(val result: BattleResult): BattleState
}

@HiltViewModel
class FortressViewModel @Inject constructor(
    stateHandle: SavedStateHandle,
    locationRepository: LocationRepository,
    authRepository: AuthRepository,
    private val usersRepository: UsersRepository,
    private val fortressesRepository: FortressesRepository,
    private val simulateBattle: SimulateBattleUseCase,
): ViewModel() {
    companion object {
        private const val BATTLE_DISTANCE_M = 10
    }

    private val fortressId: String = stateHandle.get<String>(MainDestination.Fortress.FORTRESS_ID_KEY)!!

    val locationFlow = locationRepository.getCurrentLocationUpdates()
        .filterIsInstance<Response.Success<Location, String>>()
        .take(1)
    val currentUserState = authRepository.authState
        .filterIsInstance<AuthState.LoggedIn<User>>()
        .take(1)

    private val _fortressState: MutableStateFlow<Response<Fortress, UiText>> = MutableStateFlow(Response.Loading)
    val fortressState = _fortressState.asStateFlow()

    private val _fortressOwnerState: MutableStateFlow<Response<User, UiText>> = MutableStateFlow(Response.Loading)
    val fortressOwnerState = _fortressOwnerState.asStateFlow()

    private val _battleState: MutableStateFlow<BattleState> = MutableStateFlow(BattleState.NotStarted)
    val battleState = _battleState.asStateFlow()

    init {
        viewModelScope.launch {
            _fortressState.emit(Response.Loading)
            _fortressOwnerState.emit(Response.Loading)

            try {
                val fortress = fortressesRepository.getFortress(fortressId)!!
                val fortressOwner = usersRepository.getUser(fortress.currentOwnerId)!!

                _fortressState.emit(Response.Success(fortress))
                _fortressOwnerState.emit(Response.Success(fortressOwner))
            } catch (e: NullPointerException) {
                _fortressState.emit(Response.Error(UiText.StringResource(R.string.error_fortress_not_found)))
                _fortressOwnerState.emit(Response.Error(UiText.StringResource(R.string.error_user_not_found)))
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
        fortress: Fortress,
        userLocation: Location,
        fortressLocation: Location
    ) {
        if (canBattle(
            currentUser.id,
            fortressOwner.id,
            userLocation,
            fortressLocation)
        ) {
            viewModelScope.launch {
                _battleState.emit(BattleState.InProgress)

                val battleTask = async {
                    val result = simulateBattle(currentUser, fortressOwner)

                    if (currentUser.id == result.winner.id) {
                        usersRepository.onBattleWin(result.winner, result.xpGained, fortress)
                    }

                    result
                }
                val simulationTask = async { delay(2000) }

                val battleResult = awaitAll(battleTask, simulationTask).first() as BattleResult
                Log.d(TAG, "Battle winner: ${battleResult.winner.firstName} ${battleResult.winner.lastName}, xp gained: ${battleResult.xpGained}")

                _battleState.emit(BattleState.Finished(battleResult))
            }
        }
    }
}