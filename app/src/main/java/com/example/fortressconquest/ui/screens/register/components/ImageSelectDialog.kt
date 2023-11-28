package com.example.fortressconquest.ui.screens.register.components

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import coil.compose.AsyncImage
import com.example.fortressconquest.R
import com.example.fortressconquest.ui.components.ChooseImageButton
import com.example.fortressconquest.ui.components.TakeImageButton

@Composable
fun ImageSelectDialog(
    imageUri: Uri,
    onImageTaken: (Uri) -> Unit,
    onImageSelected: (Uri) -> Unit,
    onCameraPermissionDenied: () -> Unit,
    modifier: Modifier = Modifier
) {
    val placeholderImage = painterResource(id = R.drawable.pfp_placeholder)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(
            dimensionResource(id = R.dimen.padding_small),
            Alignment.CenterVertically
        ),
        modifier = modifier
    ) {
        AsyncImage(
            model = imageUri,
            contentDescription = stringResource(id = R.string.profile_picture),
            placeholder = placeholderImage,
            error = placeholderImage,
            fallback = placeholderImage,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(dimensionResource(id = R.dimen.image_pfp_size))
                .clip(CircleShape)
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