package com.example.fortressconquest.ui.screens.map

import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fortressconquest.domain.model.Response
import com.example.fortressconquest.domain.repository.LocationRepository
import com.example.fortressconquest.domain.usecase.GetCurrentUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "MapViewModel"

sealed interface LocationState {
    data class PermissionsNotGranted(val permanently: Boolean): LocationState
    object Loading: LocationState
    data class Success(val location: Location): LocationState
    data class Error(val error: String): LocationState
}

@HiltViewModel
class MapViewModel @Inject constructor(
    private val locationRepository: LocationRepository,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
): ViewModel() {

    private val _locationState: MutableStateFlow<LocationState> = MutableStateFlow(LocationState.Loading)
    val locationState = _locationState.asStateFlow()

    fun onLocationGranted() {
        Log.d(TAG, "Starting fetching of location")

        _locationState.update { LocationState.Loading }

        viewModelScope.launch {
            _locationState.update { getCurrentLocation() }
        }
    }

    fun onLocationDenied(permanently: Boolean) {
        _locationState.update { LocationState.PermissionsNotGranted(permanently) }
    }

    private suspend fun getCurrentLocation(): LocationState =
        when (val location = locationRepository.getCurrentLocation()) {
            is Response.Success ->  LocationState.Success(location.data)
            is Response.Error -> LocationState.Error(location.error)
            else -> LocationState.Loading
        }
}