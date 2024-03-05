package com.example.weathers.util

sealed class LoadTask<out T> {
    data class Error(val errorMessage: String) : LoadTask<Nothing>()

    data class Success<out T>(val data: T) : LoadTask<T>()
}