package com.example.fortressconquest.domain.utils

sealed class Response<out T, out R> {
    object None: Response<Nothing, Nothing>()

    object Loading: Response<Nothing, Nothing>()

    data class Success<out T, out R>(
        val data: T
    ): Response<T, R>()

    data class Error<out T, out R>(
        val error: R
    ): Response<T, R>()
}