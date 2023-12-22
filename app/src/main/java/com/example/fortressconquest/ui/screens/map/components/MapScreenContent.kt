package com.example.fortressconquest.ui.screens.map.components

import android.location.Location
import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fortressconquest.domain.model.Response
import com.example.fortressconquest.ui.components.LoadingScreen
import com.example.fortressconquest.ui.screens.map.MapViewModel
import com.example.fortressconquest.ui.screens.map.TAG
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.LocationSource
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState

private const val DEFAULT_ZOOM = 19f

@Composable
fun MapScreenContent(
    onLocationError: suspend (String) -> Unit,
    modifier: Modifier = Modifier,
    mapViewModel: MapViewModel = hiltViewModel(),
) {
    var cameraLocked by remember { mutableStateOf(false) }
    val cameraPositionState = rememberCameraPositionState()
    val mapProperties by remember {
        mutableStateOf(
            MapProperties(
                isMyLocationEnabled = true
            )
        )
    }
    val mapUiSettings by remember {
        mutableStateOf(
            MapUiSettings(
                zoomControlsEnabled = false,
                myLocationButtonEnabled = false
            )
        )
    }

    val locationState by mapViewModel.locationFlow.collectAsStateWithLifecycle(Response.Loading)
    val initialLocationState by mapViewModel.initialLocation.collectAsStateWithLifecycle(Response.Loading)
    val mapContentState by mapViewModel.mapContentState.collectAsStateWithLifecycle(Response.Loading)

    val locationSource = remember {
        object : LocationSource {
            private var listener: LocationSource.OnLocationChangedListener? = null

            override fun activate(listener: LocationSource.OnLocationChangedListener) {
                this.listener = listener
            }

            override fun deactivate() {
                listener = null
            }

            fun onNewLocation(location: Location) {
                listener?.onLocationChanged(location)
            }
        }
    }

    LaunchedEffect(initialLocationState) {
        val initialLocation = initialLocationState
        if (initialLocation is Response.Success) {
            Log.d(TAG, "Initial location: ${initialLocation.data.latitude}, ${initialLocation.data.longitude}")
            cameraPositionState.move(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(
                        initialLocation.data.latitude,
                        initialLocation.data.longitude
                    ),
                    DEFAULT_ZOOM
                )
            )
        }
    }

    LaunchedEffect(locationState) {
        val location = locationState
        if (location is Response.Success) {
            locationSource.onNewLocation(location.data)

//            cameraPositionState.animate(
//                CameraUpdateFactory.newLatLng(
//                    LatLng(location.data.latitude, location.data.longitude)
//                )
//            )
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = modifier.matchParentSize(),
            cameraPositionState = cameraPositionState,
            locationSource = locationSource,
            properties = mapProperties,
            uiSettings = mapUiSettings,
            onMapLoaded = {
                mapViewModel.onMapLoaded()
            }
        ) {
        }

        AnimatedContent(
            targetState = mapContentState,
            label = "Map content animated content"
        ) { targetState ->
            when (targetState) {
                is Response.Loading -> {
                    LoadingScreen(
                        modifier = modifier.background(color = MaterialTheme.colorScheme.surface)
                    )
                }
                is Response.Error -> {
                    LaunchedEffect(targetState) {
                        onLocationError(targetState.error)
                    }

                    LocationError(
                        text = targetState.error,
                        modifier = modifier.background(color = MaterialTheme.colorScheme.surface)
                    )
                }
                else -> Unit
            }

        }
    }
}