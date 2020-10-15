package com.alegator1209.utils

sealed class Result<out T> {
    data class Success<T>(val data: T): Result<T>()
    data class Failure(val error: Exception): Result<Nothing>()
}