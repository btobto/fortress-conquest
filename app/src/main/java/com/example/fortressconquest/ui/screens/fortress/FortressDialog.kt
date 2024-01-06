package com.example.fortressconquest.ui.screens.fortress

import android.location.Location
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.fortressconquest.domain.model.Response
import com.example.fortressconquest.ui.components.LoadingIndicatorWithText
import com.example.fortressconquest.ui.screens.fortress.components.FortressDialogContent

@Composable
fun FortressDialog(
    modifier: Modifier = Modifier,
    fortressViewModel: FortressViewModel = hiltViewModel(),
) {
    val locationState by fortressViewModel.locationFlow.collectAsStateWithLifecycle(Response.Loading)
    val currentUserState by fortressViewModel.currentUserState.collectAsStateWithLifecycle(AuthState.Loading)
    val fortressState by fortressViewModel.fortressState.collectAsStateWithLifecycle()
    val fortressOwnerState by fortressViewModel.fortressOwnerState.collectAsStateWithLifecycle()
    val battleState by fortressViewModel.battleState.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .size(width = 200.dp, height = 250.dp)
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(dimensionResource(id = R.dimen.padding_medium)),
    ) {
        val location = locationState
        val currentUser = currentUserState
        val fortress = fortressState
        val fortressOwner = fortressOwnerState

        if (
            location is Response.Success &&
            currentUser is AuthState.LoggedIn &&
            fortress is Response.Success &&
            fortressOwner is Response.Success
        ) {
            AnimatedContent(
                targetState = battleState,
                label = "Battle state animated content",
                modifier = Modifier.fillMaxSize()
            ) { targetState ->
                when (targetState) {
                    BattleState.NotStarted -> {
                        val fortressLocation = Location("").apply {
                            latitude = fortress.data.latitude
                            longitude = fortress.data.longitude
                        }

                        FortressDialogContent(
                            fortress = fortress.data,
                            owner = fortressOwner.data,
                            onBattle = {
                                fortressViewModel.battle(
                                    currentUser.data,
                                    fortressOwner.data,
                                    fortress.data,
                                    location.data,
                                    fortressLocation
                                )
                            },
                            battleAllowed = fortressViewModel.canBattle(
                                currentUser.data.id,
                                fortressOwner.data.id,
                                location.data,
                                fortressLocation
                            )
                        )
                    }
                    is BattleState.Finished -> {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(
                                dimensionResource(id = R.dimen.padding_small),
                                Alignment.CenterVertically
                            )
                        ) {
                            if (targetState.result.winner.id == currentUser.data.id) {
                                Text(
                                    text = stringResource(id = R.string.battle_won_title),
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(text = stringResource(R.string.battle_won_text, targetState.result.xpGained))
                            } else {
                                Text(
                                    text = stringResource(R.string.battle_lost_title),
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(text = stringResource(id = R.string.battle_lost_text))
                            }
                        }
                    }
                    BattleState.InProgress -> {
                        LoadingIndicatorWithText(
                            textId = R.string.battle_in_progress
                        )
                    }
                }
            }
        }
    }
}