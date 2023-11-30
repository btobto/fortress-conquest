package com.example.fortressconquest.ui.screens.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.example.fortressconquest.R

@Composable
fun PaginationSelectButtons(
    onSelected: () -> Unit,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier
        .clip(RoundedCornerShape(100))
        .background(MaterialTheme.colorScheme.surfaceVariant)
        .padding(dimensionResource(id = R.dimen.padding_small))
    ) {
        IconButton(
            onClick = onPrevious
        ) {
            Icon(
                imageVector = Icons.Filled.KeyboardArrowLeft,
                contentDescription = stringResource(id = R.string.previous_entry),
            )
        }

        TextButton(
            onClick = onSelected
        ) {
            Text(text = stringResource(id = R.string.select))
        }

        IconButton(
            onClick = onNext
        ) {
            Icon(
                imageVector = Icons.Filled.KeyboardArrowRight,
                contentDescription = stringResource(id = R.string.next_entry),
            )
        }
    }
}