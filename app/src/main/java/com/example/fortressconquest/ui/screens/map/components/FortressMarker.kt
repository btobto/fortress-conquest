package com.example.fortressconquest.ui.screens.map.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.graphics.drawable.toBitmap
import com.example.fortressconquest.R
import com.example.fortressconquest.domain.model.Fortress
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.compose.GoogleMapComposable
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState

@Composable
@GoogleMapComposable
fun FortressMarker(
    state: MarkerState,
    fortress: Fortress,
    onClick: (Marker) -> Boolean = { false }
) {
    val context = LocalContext.current

    Marker(
        state = state,
        title = fortress.currentOwnerId,
        snippet = "Owned since: ${fortress.ownedSince}",
        onClick = onClick,
        icon = BitmapDescriptorFactory.fromBitmap(
            context.resources.getDrawable(R.drawable.fort_24, null).toBitmap()
        )
    )
}