package com.example.fortressconquest.ui.screens.map.components

import android.location.Location
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fortressconquest.domain.model.Response
import com.example.fortressconquest.ui.components.LoadingScreen

@Composable
fun MapContent(
    currentLocationResponse: Response<Location, String>,
    onLocationError: suspend (String) -> Unit,
    modifier: Modifier = Modifier
) {
    when (currentLocationResponse) {
        is Response.Success -> {
            Map(
                currentLocation = currentLocationResponse.data,
                modifier = modifier
            )
        }
        else -> {
            if (currentLocationResponse is Response.Error) {
                LaunchedEffect(currentLocationResponse) {
                    onLocationError(currentLocationResponse.error)
                }
            }

            LoadingScreen(modifier = modifier)
        }
    }
}