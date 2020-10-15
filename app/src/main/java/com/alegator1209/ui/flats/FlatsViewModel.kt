package com.alegator1209.ui.flats

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alegator1209.interactors.FlatsUseCases
import com.alegator1209.model.Flat
import com.alegator1209.utils.Result
import kotlinx.coroutines.launch
import java.lang.Exception

class FlatsViewModel : ViewModel() {
    lateinit var flatsUseCases: FlatsUseCases

    private val _flats = MutableLiveData<List<Flat>>()
    val flats: LiveData<List<Flat>> = _flats

    private val _refreshing = MutableLiveData<Boolean>(false)
    val refreshing: LiveData<Boolean> = _refreshing

    private var onRefreshFailed: (reason: Exception) -> Unit = {}
    private var onAddFlatFailed: (reason: Exception) -> Unit = {}

    fun setOnRefreshFailed(f: (reason: Exception) -> Unit) { onRefreshFailed = f }
    fun setOnAddFlatFailed(f: (reason: Exception) -> Unit) { onAddFlatFailed = f }

    fun refresh() {
        _refreshing.value = true
        viewModelScope.launch {
            when (val result = flatsUseCases.getFlats()) {
                is Result.Success -> _flats.value = result.data
                is Result.Failure -> onRefreshFailed(result.error)
            }
            _refreshing.value = false
        }
    }

    private fun addFlat(flat: Flat) {
        _refreshing.value = true
        viewModelScope.launch {
            when (val result = flatsUseCases.addFlat(flat)) {
                is Result.Success -> _flats.value = _flats.value?.let {
                    val new = it.toMutableList()
                    new.add(result.data)
                    new
                } ?: _flats.value

                is Result.Failure -> onAddFlatFailed(result.error)
            }
            _refreshing.value = false
        }
    }

    fun addFlat(name: String, address: String) = addFlat(Flat(0, name, address))
    fun getRoomsUseCases(flat: Flat) = flatsUseCases.toRoomsUseCases(flat)
}
