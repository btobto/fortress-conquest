package com.example.fortressconquest.ui.screens.map

import android.Manifest
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import com.example.fortressconquest.R
import com.example.fortressconquest.common.createOpenAppSettingsIntent
import com.example.fortressconquest.common.findActivity
import com.example.fortressconquest.ui.components.LoadingScreen
import com.example.fortressconquest.ui.screens.map.components.LocationErrorWithButton
import com.example.fortressconquest.ui.screens.map.components.MapScreenContent

internal const val TAG = "MapScreen"

private sealed interface PermissionsState {
    object Granted : PermissionsState
    object Denied : PermissionsState
    object PermanentlyDenied : PermissionsState
    object Loading : PermissionsState
}

@Composable
fun MapScreen(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    var permissionsState: PermissionsState by remember {
        mutableStateOf(PermissionsState.Loading)
    }

    val locationPermissionsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            permissionsState = when {
                permissions.all { it.value } -> {
                    PermissionsState.Granted
                }
                permissions.keys.any {!shouldShowRequestPermissionRationale(context.findActivity(), it)} -> {
                    PermissionsState.PermanentlyDenied
                }
                else -> {
                    PermissionsState.Denied
                }
            }
        }
    )

    val openAppSettingsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = {
            Log.d(TAG, "Returned from settings, requesting permissions")
            locationPermissionsLauncher.launchLocationPermissions()
        }
    )

    LaunchedEffect(Unit) {
        locationPermissionsLauncher.launchLocationPermissions()
    }

    AnimatedContent(
        targetState = permissionsState,
        label = "Permissions animated content"
    ) { targetState ->
        when (targetState) {
            PermissionsState.Denied -> LocationErrorWithButton(
                text = stringResource(id = R.string.error_loc_perms_denied_verbose),
                buttonLabel = stringResource(R.string.grant_permissions),
                icon = Icons.Outlined.WarningAmber,
                onClick = { locationPermissionsLauncher.launchLocationPermissions() },
                modifier = modifier
            )
            PermissionsState.PermanentlyDenied -> LocationErrorWithButton(
                text = stringResource(id = R.string.error_loc_perms_denied_verbose),
                buttonLabel = stringResource(R.string.open_settings),
                icon = Icons.Outlined.WarningAmber,
                onClick = {
                    val intent = context.findActivity().createOpenAppSettingsIntent()
                    openAppSettingsLauncher.launch(intent)
                },
                modifier = modifier
            )
            PermissionsState.Loading -> LoadingScreen(modifier = modifier)
            PermissionsState.Granted -> MapScreenContent(
                modifier = modifier,
                onProfileButtonClicked = {},
                onLeaderboardButtonClicked = {},
                onLogOutButtonClicked = {}
            )
        }
    }
}

private fun ManagedActivityResultLauncher<Array<String>, Map<String, Boolean>>.launchLocationPermissions() {
    this.launch(
        arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )
}