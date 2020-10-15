package com.alegator1209.model

import com.alegator1209.utils.Result

interface DataSource {
    suspend fun getFlats(): Result<List<Flat>>
    suspend fun addFlat(flat: Flat): Result<Flat>

    suspend fun getRooms(flatId: Int): Result<List<Room>>
    suspend fun getRoom(roomId: Int): Result<Room>
    suspend fun addRoom(room: Room): Result<Room>
    suspend fun updateRoom(room: Room): Result<Unit>
    suspend fun deleteRoom(roomId: Int): Result<Unit>
}
