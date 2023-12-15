package com.example.fortressconquest.ui.screens.map

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fortressconquest.domain.model.Response
import com.example.fortressconquest.domain.repository.LocationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface LocationState {
    data class PermissionsNotGranted(val permanently: Boolean): LocationState
    object Loading: LocationState
    data class Success(val location: Location): LocationState
    data class Error(val error: String): LocationState
}

@HiltViewModel
class MapViewModel @Inject constructor(
    private val locationRepository: LocationRepository
): ViewModel() {

    private val _locationState: MutableStateFlow<LocationState> = MutableStateFlow(LocationState.Loading)
    val locationState = _locationState.asStateFlow()

    fun onLocationGranted() {
        _locationState.value = LocationState.Loading

        viewModelScope.launch {
            locationRepository.getCurrentLocation()
                .map { response ->
                    when (response) {
                        is Response.Success -> LocationState.Success(response.data)
                        is Response.Error -> LocationState.Error(response.error)
                        else -> LocationState.Loading
                    }
                }
                .collect {
                    _locationState.value = it
                }
        }
    }

    fun onLocationDenied(permanently: Boolean) {
        _locationState.value = LocationState.PermissionsNotGranted(permanently)
    }
}