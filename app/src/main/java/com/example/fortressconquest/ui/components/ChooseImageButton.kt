package com.example.fortressconquest.ui.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.fortressconquest.R

@Composable
fun ChooseImageButton(
    onChosenImage: (Uri) -> Unit,
    modifier: Modifier = Modifier
) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { result -> onChosenImage(result ?: Uri.EMPTY) }
    )

    FilledTonalButtonWithIcon(
        onClick = { launcher.launch("image/*") },
        icon = Icons.Outlined.Image,
        labelId = R.string.button_choose_gallery,
        modifier = modifier
    )
}