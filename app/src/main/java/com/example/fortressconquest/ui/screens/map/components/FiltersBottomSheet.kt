package com.example.fortressconquest.ui.screens.map.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.example.fortressconquest.R
import com.example.fortressconquest.domain.model.Filters
import com.example.fortressconquest.ui.components.LabeledRangeSlider
import com.example.fortressconquest.ui.components.LabeledSlider
import com.example.fortressconquest.ui.components.OutlinedButtonWithIcon
import kotlinx.coroutines.launch

val defaultRadiusRange = 100f..1000f
val defaultLevelRange = 1f..100f

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FiltersBottomSheet(
    initialValue: Filters,
    onApplyFilters: (Filters) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val filtersSheetState = rememberModalBottomSheetState()
    var filters by remember { mutableStateOf(initialValue.copy()) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = filtersSheetState,
        modifier = modifier
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium)),
            modifier = Modifier.padding(
                start = dimensionResource(id = R.dimen.padding_large),
                end = dimensionResource(id = R.dimen.padding_large),
                bottom =  dimensionResource(id = R.dimen.padding_large),
                top = dimensionResource(id = R.dimen.padding_small)
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(id = R.dimen.padding_medium)),
                horizontalArrangement = Arrangement.Center
            ) {
                OutlinedButtonWithIcon(
                    textId = R.string.apply,
                    icon = Icons.Outlined.Check,
                    onClick = {
                        scope.launch {
                            filtersSheetState.hide()
                        }.invokeOnCompletion {
                            onApplyFilters(filters)
                        }
                    }
                )

                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.padding_medium) ))

                OutlinedButtonWithIcon(
                    textId = R.string.cancel,
                    icon = Icons.Outlined.Close,
                    onClick = {
                        scope.launch {
                            filtersSheetState.hide()
                        }.invokeOnCompletion {
                            onDismiss()
                        }
                    }
                )
            }

            LabeledSlider(
                label = stringResource(id = R.string.radius),
                valueFormatter = { radius -> "${radius.toInt()}m" },
                valueRange = defaultRadiusRange,
                steps = 8,
                onValueChangeFinished = { radius ->
                    filters = filters.copy(radiusInM = radius.toDouble())
                },
                initialValue = filters.radiusInM.toFloat()
            )

            LabeledRangeSlider(
                label = stringResource(R.string.opponent_level),
                valueFormatter = { start, end -> context.getString(R.string.level_range, start.toInt(), end.toInt()) },
                valueRange = defaultLevelRange,
                onValueChangeFinished = { range ->
                    filters = filters.copy(levelRange = IntRange(range.start.toInt(), range.endInclusive.toInt()))
                },
                initialValue = filters.levelRange.first.toFloat()..filters.levelRange.last.toFloat()
            )
        }
    }
}