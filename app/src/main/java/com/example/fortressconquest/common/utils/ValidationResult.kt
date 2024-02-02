package com.example.fortressconquest.common.utils

sealed interface ValidationResult {
    object Success: ValidationResult
    data class Error(val error: UiText): ValidationResult
}