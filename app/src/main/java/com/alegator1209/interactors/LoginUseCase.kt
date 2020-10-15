package com.alegator1209.interactors

import com.alegator1209.utils.Result

class LoginUseCase(
    private val username: String,
    private val password: String,
    private val authenticator: Authenticator
) {
    suspend fun login(): Result<String> = authenticator.getToken(username, password)
}
