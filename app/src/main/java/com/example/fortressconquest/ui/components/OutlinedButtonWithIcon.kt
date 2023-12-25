package com.example.fortressconquest.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fortressconquest.R

@Composable
fun OutlinedButtonWithIcon(
    @StringRes textId: Int,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null
        )
        Spacer(modifier = Modifier.size(dimensionResource(id = R.dimen.padding_small)))
        Text(
            text = stringResource(textId),
            modifier = Modifier.padding(end = 6.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun OutlinedButtonWithIconPreview() {
    OutlinedButtonWithIcon(
        textId = R.string.apply,
        icon = Icons.Outlined.Check,
        onClick = { }
    )
}