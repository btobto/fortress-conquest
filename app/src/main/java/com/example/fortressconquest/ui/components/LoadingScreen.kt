package com.example.fortressconquest.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.fortressconquest.R

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    LoadingIndicatorWithText(
        textId = R.string.loading,
        modifier = modifier.fillMaxSize()
    )
}