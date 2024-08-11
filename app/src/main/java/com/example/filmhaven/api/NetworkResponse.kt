package com.example.filmhaven.api

sealed class NetworkResponse<out T> {
    data class Success<out T>(val data: T) : NetworkResponse<T>()
    data class Error<out T>(val message: T) : NetworkResponse<Nothing>()
    data object Loading : NetworkResponse<Nothing>()
}