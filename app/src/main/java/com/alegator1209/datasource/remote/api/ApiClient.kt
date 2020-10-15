package com.alegator1209.datasource.remote.api

import com.alegator1209.utils.Constants
import com.alegator1209.interactors.Authenticator
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient {
    private lateinit var apiService: FlatsApi

    fun getApiService(): FlatsApi {
        if (!::apiService.isInitialized) {
            val retrofit = Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            apiService = retrofit.create(FlatsApi::class.java)
        }

        return apiService
    }

    fun getAuth(): Authenticator = FlatsApiAuth(getApiService())
}