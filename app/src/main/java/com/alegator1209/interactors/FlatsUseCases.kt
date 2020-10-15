package com.alegator1209.interactors

import com.alegator1209.model.DataSource
import com.alegator1209.model.Flat
import com.alegator1209.utils.Result

class FlatsUseCases(private val dataSource: DataSource) {
    suspend fun getFlats(): Result<List<Flat>> = dataSource.getFlats()
    suspend fun addFlat(flat: Flat): Result<Flat> = dataSource.addFlat(flat)
    fun toRoomsUseCases(flat: Flat) = FlatRoomsUseCases(dataSource, flat)
}
