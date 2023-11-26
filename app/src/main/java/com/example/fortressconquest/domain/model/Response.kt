package com.example.fortressconquest.domain.model

import java.lang.Exception

sealed class Response<out T> {
    object Loading: Response<Nothing>()

    data class Success<out T>(
        val data: T
    ): Response<T>()

    data class Error(
        val error: String
    ): Response<Nothing>()
}