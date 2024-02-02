package com.example.fortressconquest.ui.screens.map

import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fortressconquest.domain.utils.AuthState
import com.example.fortressconquest.domain.model.Filters
import com.example.fortressconquest.domain.model.Fortress
import com.example.fortressconquest.domain.utils.Response
import com.example.fortressconquest.domain.model.User
import com.example.fortressconquest.domain.repository.AuthRepository
import com.example.fortressconquest.domain.repository.FortressesRepository
import com.example.fortressconquest.domain.repository.LocationRepository
import com.example.fortressconquest.domain.usecase.GetFortressesFilteredUseCase
import com.example.fortressconquest.ui.screens.map.components.defaultLevelRange
import com.example.fortressconquest.ui.screens.map.components.defaultRadiusRange
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    locationRepository: LocationRepository,
    private val getFortressesFilteredUseCase: GetFortressesFilteredUseCase,
    private val fortressesRepository: FortressesRepository,
    private val authRepository: AuthRepository
): ViewModel() {

    private var isMapLoaded = false

    val locationFlow = locationRepository.getCurrentLocationUpdates()
    val initialLocation = locationFlow
        .filterIsInstance<Response.Success<Location, String>>()
        .take(1)

    val mapContentState = locationFlow
        .map { state ->
            when {
                state is Response.Error -> state
                state is Response.Loading || !isMapLoaded -> Response.Loading
                else -> state
            }
        }
        .distinctUntilChanged { old, new ->
            old is Response.Success && new is Response.Success
        }

    val currentUserState = authRepository.authState
        .onEach {
            if (it is AuthState.LoggedIn) {
                 Log.d(TAG, "character: ${it.data.character ?: "null"}")
            }
        }

    private val _filtersState = MutableStateFlow(
        Filters(
            radiusInM = defaultRadiusRange.start.toDouble(),
            levelRange = defaultLevelRange.start.toInt()..defaultLevelRange.endInclusive.toInt()
        )
    )
    val filtersState = _filtersState.asStateFlow()

    private val _fortressesState = MutableStateFlow<List<Fortress>>(emptyList())
    val fortressesState = _fortressesState.asStateFlow()

    private val _fortressesFlow: Flow<List<Fortress>> = locationFlow
        .filterIsInstance<Response.Success<Location, String>>()
        .map { it.data }
        .combine(filtersState) { location, filters -> location to filters }
        .distinctUntilChanged { (oldLocation, oldFilters), (newLocation, newFilters) ->
            oldLocation.distanceTo(newLocation) < oldFilters.radiusInM / 2 &&
            oldFilters == newFilters
        }
        .onEach { (location, filters) ->
            Log.d(TAG, "Location: ${location.latitude}, ${location.longitude}, Filters: ${filters.radiusInM}, ${filters.levelRange}")
        }
        .map { (location, filters) -> getFortressesFilteredUseCase(location, filters) }
        .onEach {  fortresses ->
            fortresses.forEach { fortress ->
                Log.d(
                    TAG,
                    "Fortress: ${fortress.id}, ${fortress.currentOwnerId}, ${fortress.ownedSince}"
                )
            }
        }

    init {
        viewModelScope.launch {
            _fortressesFlow.collect { fortresses ->
                _fortressesState.emit(fortresses)
            }
        }
    }

    fun applyFilters(filters: Filters) {
        viewModelScope.launch {
            _filtersState.emit(filters)
        }
    }

    fun onMapLoaded() {
        isMapLoaded = true
    }

    fun placeFortress(user: User, location: Location) {
        viewModelScope.launch {
            fortressesRepository.setFortress(user, location)
            _fortressesState.emit(
                getFortressesFilteredUseCase(location, filtersState.value)
            )
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }
}