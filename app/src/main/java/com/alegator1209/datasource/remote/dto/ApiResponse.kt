package com.alegator1209.datasource.remote.dto

data class ApiResponse<T>(
    val value: T,
    val success: Boolean,
    val error: ApiError
)
