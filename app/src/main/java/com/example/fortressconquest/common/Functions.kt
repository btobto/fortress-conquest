package com.example.fortressconquest.common

import android.util.Patterns
import com.example.fortressconquest.R
import com.example.fortressconquest.common.model.UiText
import com.example.fortressconquest.common.model.ValidationResult

fun validateEmail(input: String): ValidationResult {
    if (input.isBlank()) {
        return ValidationResult.Error(
            UiText.StringResource(
                resId = R.string.error_email_empty
            )
        )
    }

    if (!Patterns.EMAIL_ADDRESS.matcher(input).matches()) {
        return ValidationResult.Error(
            UiText.StringResource(
                resId = R.string.error_email_invalid
            )
        )
    }

    return ValidationResult.Success
}

fun validatePassword(input: String): ValidationResult {
    if (input.length < Constants.PASSWORD_MIN_LENGTH) {
        return ValidationResult.Error(
            UiText.StringResource(
                resId = R.string.error_password_short,
                args = arrayOf(Constants.PASSWORD_MIN_LENGTH)
            )
        )
    }

    return ValidationResult.Success
}

fun validatePhoneNumber(input: String): ValidationResult {
    if (input.isBlank()) {
        return ValidationResult.Error(
            UiText.StringResource(
                resId = R.string.error_phone_empty
            )
        )
    }

    if (!Regex("^\\d+\$").matches(input)) {
        return ValidationResult.Error(
            UiText.StringResource(
                resId = R.string.error_phone_non_digits
            )
        )
    }

    return ValidationResult.Success
}

fun getErrorText(validationResult: ValidationResult): UiText? {
    return when (validationResult) {
        is ValidationResult.Error -> validationResult.error
        is ValidationResult.Success -> null
    }
}