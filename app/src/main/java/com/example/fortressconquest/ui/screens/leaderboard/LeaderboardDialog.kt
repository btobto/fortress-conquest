package com.example.fortressconquest.ui.screens.leaderboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.fortressconquest.R

@Composable
fun LeaderboardDialog(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    leaderboardViewModel: LeaderboardViewModel = hiltViewModel(),
) {


    Dialog(onDismissRequest = onDismiss) {
       Column(
           modifier = modifier
               .size(400.dp)
               .background(
                   color = MaterialTheme.colorScheme.surface,
                   shape = RoundedCornerShape(16.dp)
               )
               .padding(dimensionResource(id = R.dimen.padding_medium)),
       ) {

       }
    }
}