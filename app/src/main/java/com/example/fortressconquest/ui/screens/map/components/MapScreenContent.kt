package com.example.fortressconquest.ui.screens.map.components

import android.location.Location
import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
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
import com.example.fortressconquest.domain.model.Response
import com.example.fortressconquest.ui.components.LoadingScreen
import com.example.fortressconquest.ui.screens.map.MapViewModel
import com.example.fortressconquest.ui.screens.map.TAG
import com.google.android.gms.maps.LocationSource
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch

private const val DEFAULT_ZOOM = 19f
private const val DEFAULT_ANIMATION_DURATION_MS = 500

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreenContent(
    onProfileButtonClicked: () -> Unit,
    onLeaderboardButtonClicked: () -> Unit,
    onLogOutButtonClicked: () -> Unit,
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

    val filtersSheetState = rememberModalBottomSheetState()
    var showFiltersSheet by remember { mutableStateOf(false) }
    var cantPlaceFortressDialog by remember { mutableStateOf(false) }
    var logoutDialog by remember { mutableStateOf(false) }

    val locationState by mapViewModel.locationFlow.collectAsStateWithLifecycle(Response.Loading)
    val initialLocationState by mapViewModel.initialLocation.collectAsStateWithLifecycle(Response.Loading)
    val mapContentState by mapViewModel.mapContentState.collectAsStateWithLifecycle(Response.Loading)
    val currentUser by mapViewModel.currentUserState.collectAsStateWithLifecycle(AuthState.Loading)

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
                    cameraPositionState.animateToLocation(location.data, DEFAULT_ZOOM)
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
            val user = currentUser
            if (location is Response.Success && user is AuthState.LoggedIn) {
                if (user.data.canSetFortress()) {
                    mapViewModel.placeFortress(user.data, location.data)
                } else {
                    cantPlaceFortressDialog = true
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
            )

            if (showFiltersSheet) {
                ModalBottomSheet(
                    onDismissRequest = { showFiltersSheet = false },
                    sheetState = filtersSheetState,
                ) {

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

            if (logoutDialog) {
                LogOutDialog(
                    onConfirm = {
                        logoutDialog = false
                        onLogOutButtonClicked()
                    },
                    onDismiss = { logoutDialog = false }
                )
            }

            if (cantPlaceFortressDialog) {
                FortressForbiddenDialog(
                    onDismiss = { cantPlaceFortressDialog = false }
                )
            }
        }
    }
}

