package com.example.fortressconquest.ui.screens.map.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.fortressconquest.R

@Composable
fun FortressForbiddenDialog(
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = { Icon(imageVector = Icons.Filled.Info, contentDescription = null) },
        title = { Text(text = stringResource(R.string.place_fortress_forbidden_title)) },
        text = { Text(text = stringResource(R.string.place_fortress_forbidden_text)) },
        confirmButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text(text = stringResource(R.string.ok))
            }
        },
    )
}