package com.example.fortressconquest.ui.screens.fortress.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.fortressconquest.R
import com.example.fortressconquest.domain.model.Fortress
import com.example.fortressconquest.domain.model.User
import com.example.fortressconquest.ui.components.UserProfilePicture

@Composable
fun FortressDialogContent(
    fortress: Fortress,
    owner: User,
    onBattle: () -> Unit,
    battleAllowed: Boolean
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(
            dimensionResource(id = R.dimen.padding_small),
            Alignment.CenterVertically
        )
    ) {
        UserProfilePicture(
            model = owner.photoUri,
            size = 100.dp
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "${owner.firstName} ${owner.lastName}",
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = stringResource(R.string.level_number, owner.level),
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Button(
            onClick = onBattle,
            enabled = battleAllowed
        ) {
            Text(text = stringResource(R.string.battle))
        }
    }
}