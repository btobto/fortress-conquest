package com.example.fortressconquest.ui.screens.map

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fortressconquest.domain.model.Response
import com.example.fortressconquest.domain.repository.LocationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val locationRepository: LocationRepository
): ViewModel() {

    private val _locationState: MutableStateFlow<Response<Location, String>> = MutableStateFlow(Response.Loading)
    val locationState = _locationState.asStateFlow()

    fun onLocationGranted() {
        viewModelScope.launch {
            locationRepository.getCurrentLocation().collect {
                _locationState.value = it
            }
        }
    }
}