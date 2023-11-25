package com.example.fortressconquest.ui.utils

import com.example.fortressconquest.common.model.UiText

data class FormField (
    val value: String,
    val error: UiText? = null
) {
    fun isValid(
        isValueValid: (String) -> Boolean = { it.isNotBlank() }
    ): Boolean {
        return isValueValid(value) && error == null
    }
}