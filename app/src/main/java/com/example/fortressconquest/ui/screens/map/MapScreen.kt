package com.example.fortressconquest.ui.screens.map

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fortressconquest.common.findActivity
import com.example.fortressconquest.common.openAppSettings
import com.example.fortressconquest.ui.screens.map.components.MapScreenContent

private val locationPermissions = arrayOf(
    Manifest.permission.ACCESS_FINE_LOCATION,
    Manifest.permission.ACCESS_COARSE_LOCATION
)

@Composable
fun MapScreen(
    onLocationError: suspend (String) -> Unit,
    modifier: Modifier = Modifier,
    mapViewModel: MapViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val locationPermissionsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            if (permissions.all { it.value }) {
                mapViewModel.onLocationGranted()
            } else {
                mapViewModel.onLocationDenied(
                    permanently = permissions.keys.any {
                        !shouldShowRequestPermissionRationale(context.findActivity(), it)
                    }
                )
            }
        }
    )

    val locationState by mapViewModel.locationState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        locationPermissionsLauncher.launch(locationPermissions)
    }

    MapScreenContent(
        locationState = locationState,
        onLocationError = onLocationError,
        onOpenAppSettings = context.findActivity()::openAppSettings,
        onRequestPermissions = { locationPermissionsLauncher.launch(locationPermissions) },
        modifier = modifier
    )
}