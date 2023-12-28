package com.example.fortressconquest.ui.screens.fortress

import android.location.Location
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.fortressconquest.R
import com.example.fortressconquest.domain.model.AuthState
import com.example.fortressconquest.domain.model.Fortress
import com.example.fortressconquest.domain.model.Response

@Composable
fun FortressDialog(
    fortress: Fortress,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    fortressViewModel: FortressViewModel = hiltViewModel(),
) {
    val locationState by fortressViewModel.locationFlow.collectAsStateWithLifecycle(Response.Loading)
    val currentUserState by fortressViewModel.currentUserState.collectAsStateWithLifecycle(AuthState.Loading)
    val fortressOwnerState by fortressViewModel.fortressOwnerState.collectAsStateWithLifecycle()

    LaunchedEffect(fortress) {
        fortressViewModel.getFortressOwner(fortress.currentOwnerId)
    }

    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = modifier
                .size(width = 200.dp, height = 250.dp)
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(dimensionResource(id = R.dimen.padding_medium)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(
                dimensionResource(id = R.dimen.padding_small),
                Alignment.CenterVertically
            )
        ) {
            val location = locationState
            val currentUser = currentUserState
            val fortressOwner = fortressOwnerState

            if (
                location is Response.Success &&
                currentUser is AuthState.LoggedIn &&
                fortressOwner is Response.Success
            ) {
                val fortressLocation = Location("").apply {
                    latitude = fortress.latitude
                    longitude = fortress.longitude
                }

                val placeholderImage = painterResource(id = R.drawable.pfp_placeholder)

                AsyncImage(
                    model = fortressOwner.data.photoUri,
                    contentDescription = stringResource(R.string.fortress_owner_profile_picture),
                    placeholder = placeholderImage,
                    error = placeholderImage,
                    fallback = placeholderImage,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = "${fortressOwner.data.firstName} ${fortressOwner.data.lastName}",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = stringResource(R.string.level_number, fortressOwner.data.level),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                Button(
                    onClick = {
                        fortressViewModel.battle(
                            currentUser.data,
                            fortressOwner.data,
                            location.data,
                            fortressLocation
                        )
                    },
                    enabled = fortressViewModel.canBattle(
                        currentUser.data.id,
                        fortressOwner.data.id,
                        location.data,
                        fortressLocation
                    )
                ) {
                    Text(text = stringResource(R.string.battle))
                }
            }
        }
    }
}