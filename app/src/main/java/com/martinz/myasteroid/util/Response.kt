package com.martinz.myasteroid.util

sealed class Response<out T> {
    data class Success<out T>(
        val data: T
    ): Response<T>()

    data class Error<out T>(
        val message: String,
        val data : T? = null
    ): Response<T>()

    object Loading : Response<Nothing>()
}