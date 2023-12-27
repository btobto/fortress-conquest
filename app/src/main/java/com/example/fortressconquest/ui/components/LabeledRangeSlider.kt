package com.example.fortressconquest.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
fun LabeledRangeSlider(
    label: String,
    onValueChangeFinished: (ClosedFloatingPointRange<Float>) -> Unit,
    modifier: Modifier = Modifier,
    valueFormatter: (Float, Float) -> String = { start, end -> "$start - $end" },
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    initialValue: ClosedFloatingPointRange<Float> = valueRange,
    steps: Int = 0
) {
    var sliderPosition by remember { mutableStateOf(initialValue) }

    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = label)
            Text(text = valueFormatter(sliderPosition.start, sliderPosition.endInclusive))
        }
        RangeSlider(
            value = sliderPosition,
            onValueChange = { sliderPosition = it },
            valueRange = valueRange,
            steps = steps,
            onValueChangeFinished = { onValueChangeFinished(sliderPosition) }
        )
    }
}