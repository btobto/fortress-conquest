package com.example.fortressconquest.common.model

sealed interface ValidationResult{
    object Success: ValidationResult
    data class Error(val error: UiText): ValidationResult
}