package com.example.fortressconquest.ui.screens.map.components

import android.location.Location
import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fortressconquest.common.animateToLocation
import com.example.fortressconquest.common.moveToLocation
import com.example.fortressconquest.domain.model.AuthState
import com.example.fortressconquest.domain.model.Fortress
import com.example.fortressconquest.domain.model.Response
import com.example.fortressconquest.domain.model.User
import com.example.fortressconquest.ui.components.LoadingScreen
import com.example.fortressconquest.ui.screens.map.MapViewModel
import com.example.fortressconquest.ui.screens.map.TAG
import com.google.android.gms.maps.LocationSource
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch

private const val DEFAULT_ZOOM = 19f
private const val DEFAULT_ANIMATION_DURATION_MS = 1000

sealed interface FortressPlacementDialogState {
    data class Allowed(val user: User, val location: Location): FortressPlacementDialogState
    object Forbidden: FortressPlacementDialogState
    object Hidden: FortressPlacementDialogState
}

@Composable
fun MapScreenContent(
    onProfileButtonClicked: () -> Unit,
    onLeaderboardButtonClicked: () -> Unit,
    onLogOutButtonClicked: () -> Unit,
    onFortressClicked: (Fortress) -> Unit,
    modifier: Modifier = Modifier,
    mapViewModel: MapViewModel = hiltViewModel(),
) {
    val scope = rememberCoroutineScope()

    var cameraLocked by remember { mutableStateOf(false) }
    val cameraPositionState = rememberCameraPositionState()
    val mapProperties by remember {
        mutableStateOf(
            MapProperties(
                isMyLocationEnabled = true
            )
        )
    }
    var mapUiSettings by remember {
        mutableStateOf(
            MapUiSettings(
                zoomControlsEnabled = false,
                myLocationButtonEnabled = false
            )
        )
    }

    var showFiltersSheet by remember { mutableStateOf(false) }
    var placeFortressDialogState: FortressPlacementDialogState by remember { mutableStateOf(FortressPlacementDialogState.Hidden) }
    var logoutDialog by remember { mutableStateOf(false) }

    val locationState by mapViewModel.locationFlow.collectAsStateWithLifecycle(Response.Loading)
    val initialLocationState by mapViewModel.initialLocation.collectAsStateWithLifecycle(Response.Loading)
    val mapContentState by mapViewModel.mapContentState.collectAsStateWithLifecycle(Response.Loading)
    val currentUserState by mapViewModel.currentUserState.collectAsStateWithLifecycle(AuthState.Loading)
    val filtersState by mapViewModel.filtersState.collectAsStateWithLifecycle()
    val fortressesState by mapViewModel.fortressesState.collectAsStateWithLifecycle()

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

    BottomMapBar(
        onProfileButtonClicked = onProfileButtonClicked,
        onLeaderboardButtonClicked = onLeaderboardButtonClicked,
        onLogOutButtonClicked = { logoutDialog = true },
        onMyLocationButtonClicked = {
            val location = locationState
            if (location is Response.Success) {
                scope.launch {
                    val currentZoom = cameraPositionState.position.zoom
                    cameraPositionState.animateToLocation(
                        location.data,
                        if (currentZoom > DEFAULT_ZOOM) currentZoom else DEFAULT_ZOOM,
                    )
                }
            }
        },
        onLockCameraButtonClicked = {
            val location = locationState
            if (location is Response.Success) {
                mapUiSettings = mapUiSettings.copy(
                    scrollGesturesEnabled = cameraLocked,
                    zoomGesturesEnabled = cameraLocked,
                    scrollGesturesEnabledDuringRotateOrZoom = cameraLocked,
                    rotationGesturesEnabled = cameraLocked,
                )
                cameraLocked = !cameraLocked

                scope.launch {
                    cameraPositionState.animateToLocation(location.data)
                }
            }
        },
        onFiltersButtonClicked = { showFiltersSheet = true },
        onPlaceFortressButtonClicked = {
            val location = locationState
            val user = currentUserState
            if (location is Response.Success && user is AuthState.LoggedIn) {
                placeFortressDialogState = if (user.data.canSetFortress()) {
                    FortressPlacementDialogState.Allowed(user.data, location.data)
                } else {
                    FortressPlacementDialogState.Forbidden
                }
            }
        },
        cameraLocked = cameraLocked,
    ) { innerPadding ->

        LaunchedEffect(initialLocationState) {
            val initialLocation = initialLocationState
            if (initialLocation is Response.Success) {
                Log.d(TAG, "Initial location: ${initialLocation.data.latitude}, ${initialLocation.data.longitude}")
                cameraPositionState.moveToLocation(initialLocation.data, DEFAULT_ZOOM)
            }
        }

        LaunchedEffect(locationState) {
            val location = locationState
            if (location is Response.Success) {
                locationSource.onNewLocation(location.data)

                if (cameraLocked) {
                    cameraPositionState.animateToLocation(location.data, DEFAULT_ANIMATION_DURATION_MS)
                }
            }
        }

        Box(modifier = modifier
            .padding(innerPadding)
            .fillMaxSize()
        ) {
            GoogleMap(
                modifier = Modifier.matchParentSize(),
                cameraPositionState = cameraPositionState,
                locationSource = locationSource,
                properties = mapProperties,
                uiSettings = mapUiSettings,
                onMapLoaded = mapViewModel::onMapLoaded
            ) {
                fortressesState.forEach { fortress ->
                    FortressMarker(
                        state = MarkerState(
                            position = LatLng(fortress.latitude, fortress.longitude),
                        ),
                        fortress = fortress,
                        onClick = {
                            Log.d(TAG, "Fortress clicked: ${fortress.id}")
                            onFortressClicked(fortress)
                            true
                        }
                    )
                }
            }

            AnimatedContent(
                targetState = mapContentState,
                label = "Map content animated content"
            ) { targetState ->
                when (targetState) {
                    is Response.Loading -> {
                        LoadingScreen(
                            modifier = Modifier.background(color = MaterialTheme.colorScheme.surface)
                        )
                    }
                    is Response.Error -> {
                        LocationError(
                            text = targetState.error,
                            modifier = Modifier
                                .fillMaxSize()
                                .background(color = MaterialTheme.colorScheme.surface)
                        )
                    }
                    else -> Unit
                }
            }

            if (showFiltersSheet) {
                FiltersBottomSheet(
                    onDismiss = {
                        showFiltersSheet = false
                    },
                    onApplyFilters = { filters ->
                        showFiltersSheet = false
                        mapViewModel.applyFilters(filters)
                    },
                    initialValue = filtersState
                )
            }

            if (logoutDialog) {
                LogOutDialog(
                    onConfirm = {
                        logoutDialog = false
                        mapViewModel.logout()
                        onLogOutButtonClicked()
                    },
                    onDismiss = { logoutDialog = false }
                )
            }

            when (val state = placeFortressDialogState) {
                is FortressPlacementDialogState.Allowed ->
                    PlaceFortressConfirmDialog(
                        fortressesLeft = state.user.let { it.level - it.fortressCount },
                        onConfirm = {
                            placeFortressDialogState = FortressPlacementDialogState.Hidden
                            mapViewModel.placeFortress(state.user, state.location)
                        },
                        onDismiss = { placeFortressDialogState = FortressPlacementDialogState.Hidden }
                    )
                is FortressPlacementDialogState.Forbidden ->
                    PlaceFortressForbiddenDialog(
                        onDismiss = { placeFortressDialogState = FortressPlacementDialogState.Hidden }
                    )
                else -> Unit
            }
        }
    }
}

