package com.example.fortressconquest.ui.screens.map

import androidx.lifecycle.ViewModel
import com.example.fortressconquest.domain.repository.LocationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    locationRepository: LocationRepository
): ViewModel() {
//    val locationState: StateFlow<Response<Location, String>> = locationRepository.getCurrentLocation()
//        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Response.Loading)


}