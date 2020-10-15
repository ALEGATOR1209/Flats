package com.alegator1209.interactors

import com.alegator1209.utils.Result

interface Authenticator {
    suspend fun getToken(username: String, password: String): Result<String>
}