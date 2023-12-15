package com.example.fortressconquest.ui.screens.map.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.fortressconquest.R
import com.example.fortressconquest.ui.components.LoadingScreen
import com.example.fortressconquest.ui.screens.map.LocationState

@Composable
fun MapScreenContent(
    locationState: LocationState,
    onLocationError: suspend (String) -> Unit,
    onOpenAppSettings: () -> Unit,
    onRequestPermissions: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedContent(
        targetState = locationState,
        label = "Map animated content"
    ) { targetState ->
        when (targetState) {
            is LocationState.Success -> Map(
                currentLocation = targetState.location,
                modifier = modifier
            )
            is LocationState.Error -> {
                LaunchedEffect(targetState) {
                    onLocationError(targetState.error)
                }

                LocationError(text = targetState.error, modifier = modifier)
            }
            is LocationState.PermissionsNotGranted -> {
                if (targetState.permanently) {
                    LocationErrorWithButton(
                        text = stringResource(id = R.string.error_loc_perms_denied_verbose),
                        buttonLabel = stringResource(R.string.open_settings),
                        icon = Icons.Outlined.WarningAmber,
                        onClick = onOpenAppSettings,
                        modifier = modifier
                    )
                } else {
                    LocationErrorWithButton(
                        text = stringResource(id = R.string.error_loc_perms_denied_verbose),
                        buttonLabel = stringResource(R.string.grant_permissions),
                        icon = Icons.Outlined.WarningAmber,
                        onClick = onRequestPermissions,
                        modifier = modifier
                    )
                }
            }
            is LocationState.Loading -> LoadingScreen(
                modifier = modifier,
                textId = R.string.loading_map
            )
        }
    }
}