package com.example.fortressconquest.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fortressconquest.R
import com.example.fortressconquest.domain.model.AuthState
import com.example.fortressconquest.ui.components.UserProfilePicture

@Composable
fun ProfileDialog(
    modifier: Modifier = Modifier,
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    val currentUserState by profileViewModel.currentUserState.collectAsStateWithLifecycle(AuthState.Loading)

    Column(
        modifier = modifier
            .size(width = 220.dp, height = 325.dp)
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(dimensionResource(id = R.dimen.padding_medium)),
    ) {
        val currentUser = currentUserState
        if (currentUser is AuthState.LoggedIn) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(
                    dimensionResource(id = R.dimen.padding_small),
                    Alignment.CenterVertically
                ),
                modifier = Modifier.fillMaxSize()
            ) {
                UserProfilePicture(
                    model = currentUser.data.photoUri,
                    size = 100.dp
                )

                Text(
                    text = "${currentUser.data.firstName} ${currentUser.data.lastName}",
                    style = MaterialTheme.typography.titleLarge
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(R.string.level_number, currentUser.data.level),
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Spacer(modifier = Modifier.size(dimensionResource(id = R.dimen.padding_small)))

                    LinearProgressIndicator(
                        progress = currentUser.data.xp.toFloat() / currentUser.data.nextLevelXp().toFloat()
                    )

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = stringResource(R.string.xp_number, currentUser.data.xp))
                        Text(text = stringResource(R.string.xp_number, currentUser.data.nextLevelXp()))
                    }
                }

                Text(text = stringResource(R.string.fortress_count, currentUser.data.let { it.level - it.fortressCount }))

                Button(onClick = profileViewModel::logout) {
                    Text(text = stringResource(R.string.log_out))
                }
            }
        }
    }
}