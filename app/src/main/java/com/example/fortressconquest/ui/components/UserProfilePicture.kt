package com.example.fortressconquest.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.fortressconquest.R

@Composable
fun UserProfilePicture(
    model: Any?,
    @DrawableRes placeholderId: Int = R.drawable.pfp_placeholder,
    size: Dp = 50.dp,
    shape: Shape = CircleShape,
) {
    val placeholderImage = painterResource(id = placeholderId)

    AsyncImage(
        model = model,
        contentDescription = stringResource(id = R.string.profile_picture),
        placeholder = placeholderImage,
        error = placeholderImage,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(size)
            .clip(shape)
    )
}