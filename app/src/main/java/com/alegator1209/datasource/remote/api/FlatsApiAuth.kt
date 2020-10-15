package com.alegator1209.datasource.remote.api

import com.alegator1209.interactors.Authenticator
import com.alegator1209.model.Token
import com.alegator1209.utils.Result
import java.lang.Exception
import java.util.*

class FlatsApiAuth(private val api: FlatsApi): Authenticator {
    companion object {
        private const val THRESHOLD = 0.95
    }

    private lateinit var token: Token
    private lateinit var lastUpdate: Date
    private val isTokenValid: Boolean
        get() = Date().before(Date(
            lastUpdate.time + token.expiresIn * (1000 * THRESHOLD.toInt())
        ))

    private val isTokenExpired: Boolean
        get() = when {
            !::token.isInitialized -> true
            !isTokenValid -> true
            else -> false
        }

    override suspend fun getToken(username: String, password: String): Result<String> = try {
        if (isTokenExpired) token = api.getToken(username, password)
        lastUpdate = Date()
        Result.Success(token.tokenString)
    } catch (e: Exception) {
        Result.Failure(e)
    }
}
