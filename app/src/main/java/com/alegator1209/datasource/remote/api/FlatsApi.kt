package com.alegator1209.datasource.remote.api

import com.alegator1209.utils.Constants
import com.alegator1209.datasource.remote.dto.ApiResponse
import com.alegator1209.model.Token
import com.alegator1209.model.Flat
import com.alegator1209.model.Room
import retrofit2.http.*

interface FlatsApi {
    @POST(Constants.AUTH_URL)
    @FormUrlEncoded
    suspend fun getToken(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("grant_type") grantType: String = "password"
    ): Token

    @GET(Constants.FLATS_URL)
    suspend fun getFlats(@Header("Authorization") token: String): ApiResponse<List<Flat>>

    @POST(Constants.FLATS_URL)
    suspend fun addFlat(
        @Header("Authorization") token: String,
        @Body flat: Flat
    ): ApiResponse<Flat>

    @GET(Constants.ROOMS_URL)
    suspend fun getRooms(
        @Header("Authorization") token: String,
        @Query("flatId") flatId: Int
    ): ApiResponse<List<Room>>

    @POST(Constants.ROOMS_URL)
    suspend fun addRoom(
        @Header("Authorization") token: String,
        @Body room: Room
    ): ApiResponse<Room>

    @GET(Constants.ROOMS_URL + "/{id}")
    suspend fun getRoom(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): ApiResponse<Room>

    @PUT(Constants.ROOMS_URL + "/{id}")
    suspend fun updateRoom(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Body room: Room
    ): ApiResponse<Unit>

    @DELETE(Constants.ROOMS_URL + "/{id}")
    suspend fun deleteRoom(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): ApiResponse<Unit>
}
