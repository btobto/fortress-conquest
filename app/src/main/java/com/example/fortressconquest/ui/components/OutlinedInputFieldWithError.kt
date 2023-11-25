package com.example.fortressconquest.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import com.example.fortressconquest.ui.utils.FormField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutlinedInputFieldWithError(
    field: FormField,
    onValueChange: (String) -> Unit,
    @StringRes label: Int,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            value = field.value,
            singleLine = true,
            label = { Text(text = stringResource(id = label)) },
            onValueChange = onValueChange,
            isError = field.error != null,
            keyboardOptions = keyboardOptions
        )
        field.error?.let {
            InputErrorText(errorMessage = it.asString())
        }
    }
}