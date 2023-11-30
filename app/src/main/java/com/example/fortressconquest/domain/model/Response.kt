package com.example.fortressconquest.domain.model

sealed class Response<out T, out R> {
    object None: Response<Nothing, Nothing>()

    object Loading: Response<Nothing, Nothing>()

    data class Success<out T>(
        val data: T
    ): Response<T, Nothing>()

    data class Error<out R>(
        val error: R
    ): Response<Nothing, R>()
}