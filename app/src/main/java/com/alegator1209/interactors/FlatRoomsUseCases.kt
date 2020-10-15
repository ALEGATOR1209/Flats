package com.alegator1209.interactors

import com.alegator1209.model.DataSource
import com.alegator1209.model.Flat
import com.alegator1209.model.Room

class FlatRoomsUseCases(
    private val dataSource: DataSource,
    val flat: Flat
) {
    suspend fun getRooms() = dataSource.getRooms(flat.id)
    suspend fun getRoom(roomId: Int) = dataSource.getRoom(roomId)
    suspend fun addRoom(room: Room) = dataSource.addRoom(room)
    suspend fun updateRoom(room: Room) = dataSource.updateRoom(room)
    suspend fun deleteRoom(room: Room) = dataSource.deleteRoom(room.id)
}