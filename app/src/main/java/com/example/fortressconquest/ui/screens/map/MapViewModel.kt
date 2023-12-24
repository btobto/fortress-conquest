package com.example.fortressconquest.ui.screens.map

import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fortressconquest.domain.model.Response
import com.example.fortressconquest.domain.model.User
import com.example.fortressconquest.domain.repository.FortressesRepository
import com.example.fortressconquest.domain.repository.LocationRepository
import com.example.fortressconquest.domain.usecase.GetCurrentUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    locationRepository: LocationRepository,
    val currentUserUseCase: GetCurrentUserUseCase,
    val fortressesRepository: FortressesRepository
): ViewModel() {

    private var isMapLoaded = false

    val locationFlow = locationRepository.getCurrentLocationUpdates()

    val initialLocation = locationFlow
        .filter { it is Response.Success }
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
        .onEach { state ->
            val msg = when (state) {
                is Response.Error -> "Error"
                is Response.Loading -> "Loading"
                is Response.Success -> "Success"
                else -> "Unknown"
            }
            Log.d(TAG, "Map content state: $msg, Is map loaded: $isMapLoaded")
        }

    val currentUserState = currentUserUseCase()

    fun onMapLoaded() {
        isMapLoaded = true
    }

    fun placeFortress(user: User, currentLocation: Location) {
        viewModelScope.launch { fortressesRepository.setFortress(user, currentLocation) }
    }
}