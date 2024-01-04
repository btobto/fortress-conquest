package com.example.fortressconquest.ui.screens.register.components

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.example.fortressconquest.R
import com.example.fortressconquest.ui.components.ChooseImageButton
import com.example.fortressconquest.ui.components.TakeImageButton
import com.example.fortressconquest.ui.components.UserProfilePicture

@Composable
fun ImageSelectDialog(
    imageUri: Uri,
    onImageTaken: (Uri) -> Unit,
    onImageSelected: (Uri) -> Unit,
    onCameraPermissionDenied: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(
            dimensionResource(id = R.dimen.padding_small),
            Alignment.CenterVertically
        ),
        modifier = modifier
    ) {
        UserProfilePicture(
            model = imageUri,
            size = dimensionResource(id = R.dimen.image_pfp_size),
        )

        Column(
            modifier = Modifier.width(IntrinsicSize.Max)
        ) {
            TakeImageButton(
                onImageTaken = onImageTaken,
                onCameraPermissionDenied = onCameraPermissionDenied,
                modifier = Modifier.fillMaxWidth()
            )
            ChooseImageButton(
                onChosenImage = onImageSelected,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}