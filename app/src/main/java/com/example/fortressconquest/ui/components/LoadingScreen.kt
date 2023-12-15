package com.example.fortressconquest.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.fortressconquest.R

@Composable
fun LoadingScreen(
    modifier: Modifier = Modifier,
    @StringRes textId: Int = R.string.loading
) {
    LoadingIndicatorWithText(
        textId = textId,
        modifier = modifier.fillMaxSize()
    )
}