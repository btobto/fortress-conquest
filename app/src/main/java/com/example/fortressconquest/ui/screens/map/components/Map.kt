package com.example.fortressconquest.ui.screens.map.components

import android.location.Location
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState

private const val DEFAULT_ZOOM = 19f

@Composable
fun Map(
    initialLocation: Location,
    modifier: Modifier = Modifier
) {
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(
                initialLocation.latitude,
                initialLocation.longitude
            ),
            DEFAULT_ZOOM
        )
    }

    GoogleMap(
        modifier = modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        properties = MapProperties(
            isMyLocationEnabled = true
        ),
        uiSettings = MapUiSettings(
            zoomControlsEnabled = false,
        ),
        onMyLocationButtonClick = {
            cameraPositionState.move(
                CameraUpdateFactory.zoomTo(DEFAULT_ZOOM)
            )
            false
        },
        onMyLocationClick = { location ->

        }
    ) {

    }
}