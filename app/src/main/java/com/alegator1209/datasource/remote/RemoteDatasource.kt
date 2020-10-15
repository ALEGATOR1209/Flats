package com.alegator1209.datasource.remote

import com.alegator1209.datasource.remote.api.FlatsApi
import com.alegator1209.datasource.remote.dto.ApiResponse
import com.alegator1209.interactors.LoginUseCase
import com.alegator1209.model.*
import com.alegator1209.utils.Result
import java.lang.Exception

class RemoteDataSource(
    private val api: FlatsApi,
    private val loginUseCase: LoginUseCase
): DataSource {

    private suspend fun<T> makeRequest(req: suspend (token: String) -> ApiResponse<T>): Result<T> {
        return when (val token = loginUseCase.login()) {
            is Result.Success -> try {
                val response = req(token.data)
                if (!response.success) throw Exception(response.error.errorCode)
                Result.Success(response.value)
            } catch (e: Exception) { Result.Failure(e) }
            is Result.Failure -> token
        }
    }

    override suspend fun getFlats(): Result<List<Flat>> = makeRequest { token ->
        api.getFlats(token)
    }

    override suspend fun addFlat(flat: Flat): Result<Flat> = makeRequest { token ->
        api.addFlat(token, flat)
    }

    override suspend fun getRooms(flatId: Int): Result<List<Room>> = makeRequest { token ->
        api.getRooms(token, flatId)
    }

    override suspend fun addRoom(room: Room): Result<Room> = makeRequest { token ->
        api.addRoom(token, room)
    }

    override suspend fun getRoom(roomId: Int): Result<Room> = makeRequest { token ->
        api.getRoom(token, roomId)
    }

    override suspend fun updateRoom(room: Room): Result<Unit> = makeRequest { token ->
        api.updateRoom(token, room.id, room)
    }

    override suspend fun deleteRoom(roomId: Int): Result<Unit> = makeRequest { token ->
        api.deleteRoom(token, roomId)
    }
}