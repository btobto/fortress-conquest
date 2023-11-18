package com.example.fortressconquest.common.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import com.example.fortressconquest.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmailInputField(
    value: String,
    onValueChange: (String) -> Unit,
    errorMessage: String? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            singleLine = true,
            label = { Text(text = stringResource(id = R.string.email)) },
            onValueChange = onValueChange,
            isError = errorMessage != null,
            keyboardOptions = keyboardOptions.copy(
                keyboardType = KeyboardType.Email
            )
        )
        if (errorMessage != null) {
            InputErrorText(errorMessage = errorMessage)
        }
    }
}