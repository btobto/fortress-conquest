package com.example.fortressconquest.ui.screens.leaderboard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.example.fortressconquest.R
import com.example.fortressconquest.ui.components.UserProfilePicture

@Composable
fun LeaderboardDialog(
    modifier: Modifier = Modifier,
    leaderboardViewModel: LeaderboardViewModel = hiltViewModel(),
) {
    val users = leaderboardViewModel.usersFlow.collectAsLazyPagingItems()

    Card(
        modifier = modifier.size(400.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column {
            Text(
                text = stringResource(R.string.leaderboard),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(id = R.dimen.padding_medium)),
                textAlign = TextAlign.Center
            )

            LazyColumn {
                items(
                    count = users.itemCount,
                    key = users.itemKey { it.id }
                ) { index ->
                    users[index]?.let { user ->
                        ListItem(
                            headlineContent = {
                                Text(text = "${user.firstName} ${user.lastName}")
                            },
                            supportingContent = {
                                Text(text = stringResource(id = R.string.level_number, user.level))
                                Text(text = stringResource(R.string.xp_number, user.xp))
                            },
                            leadingContent = { UserProfilePicture(model = user.photoUri) },
                            colors = ListItemDefaults.colors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                            )
                        )
                    }
                }
            }
        }
    }
}