package com.example.fortressconquest.ui.screens.character_select.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.example.fortressconquest.R

@Composable
fun CharacterClassStats(
    stats: List<Pair<Int, Number>>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        stats.forEach { (name, value) ->
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "${stringResource(id = name)}:",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.End,
                    modifier = Modifier.weight(1.5f)
                )

                Spacer(modifier = Modifier.size(dimensionResource(id = R.dimen.padding_small)))

                Text(
                    text = value.toString(),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}