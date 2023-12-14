package com.example.fortressconquest.ui.screens.map

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fortressconquest.R
import com.example.fortressconquest.common.checkPermissions
import com.example.fortressconquest.common.showToast
import com.example.fortressconquest.ui.components.LoadingScreen
import com.example.fortressconquest.ui.screens.map.components.MapContent

@Composable
fun MapScreen(
    onLocationError: suspend (String) -> Unit,
    modifier: Modifier = Modifier,
    mapViewModel: MapViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val locationPermissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    var permissionsState by rememberSaveable {
        mutableStateOf(checkPermissions(context, locationPermissions))
    }

    val locationPermissionsResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            val granted = permissions.values.all { it }
            permissionsState = granted
            if (granted) {
                mapViewModel.onLocationGranted()
            } else {
                showToast(context, R.string.error_loc_perms_denied)
            }
        }
    )

    if (permissionsState) {
        val currentLocationResponse by mapViewModel.locationState.collectAsStateWithLifecycle()
        MapContent(
            currentLocationResponse = currentLocationResponse,
            onLocationError = onLocationError,
            modifier = modifier.fillMaxSize()
        )
    } else {
        SideEffect {
            locationPermissionsResultLauncher.launch(locationPermissions)
        }

        LoadingScreen(
            modifier = Modifier.fillMaxSize()
        )
    }
}

