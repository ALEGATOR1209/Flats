package com.alegator1209.ui.rooms

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alegator1209.interactors.FlatRoomsUseCases
import com.alegator1209.model.Flat
import com.alegator1209.model.Room
import com.alegator1209.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class RoomsViewModel : ViewModel() {
    private lateinit var useCases: FlatRoomsUseCases

    private val _refreshing = MutableLiveData<Boolean>(false)
    val refreshing: LiveData<Boolean> = _refreshing

    val flat: Flat
        get() = useCases.flat

    private val _rooms = MutableLiveData<List<Room>>()
    val rooms: LiveData<List<Room>> = _rooms

    fun setUseCases(useCases: FlatRoomsUseCases?) {
        if (this::useCases.isInitialized || useCases == null) return
        this.useCases = useCases
    }

    private var onRefreshFailed: (reason: Exception) -> Unit = {}
    private var onAddRoomFailed: (reason: Exception) -> Unit = {}
    private var onRoomAdded: (room: Room) -> Unit = {}
    private var onRoomUpdated: (room: Room) -> Unit = {}
    private var onRoomUpdateFailed: (reason: Exception) -> Unit = {}
    private var onRoomDeleted: (room: Room) -> Unit = {}
    private var onRoomDeleteFailed: (reason: Exception) -> Unit = {}

    fun setOnRefreshFailed(f: (reason: Exception) -> Unit) { onRefreshFailed = f }
    fun setOnRoomAdded(f: (room: Room) -> Unit) { onRoomAdded = f }
    fun setOnAddRoomFailed(f: (reason: Exception) -> Unit) { onAddRoomFailed = f }
    fun setOnRoomUpdated(f: (room: Room) -> Unit) { onRoomUpdated = f }
    fun setOnRoomUpdateFailed(f: (reason: Exception) -> Unit) { onRoomUpdateFailed = f }
    fun setOnRoomDeleted(f: (room: Room) -> Unit) { onRoomDeleted = f }
    fun setOnRoomDeleteFailed(f: (reason: Exception) -> Unit) { onRoomDeleteFailed = f }

    fun refresh() {
        _refreshing.value = true
        viewModelScope.launch {
            when (val result = useCases.getRooms()) {
                is Result.Success -> _rooms.value = result.data
                is Result.Failure -> onRefreshFailed(result.error)
            }
            _refreshing.value = false
        }
    }

    fun addRoom(name: String) {
        _refreshing.value = true
        viewModelScope.launch {
            val room = Room(0, name, flat.id)
            when (val result = useCases.addRoom(room)) {
                is Result.Success -> _rooms.value = _rooms.value?.let {
                    val new = it.toMutableList()
                    new.add(result.data)
                    new
                }
                is Result.Failure -> onAddRoomFailed(result.error)
            }
            _refreshing.value = false
        }
    }

    private fun editRoom(room: Room) {
        _refreshing.value = true
        viewModelScope.launch {
            when (val res = useCases.updateRoom(room)) {
                is Result.Success -> onRoomUpdated(room)
                is Result.Failure -> onRoomUpdateFailed(res.error)
            }
            _refreshing.value = false
        }
    }

    fun editRoom(roomId: Int, newName: String) = editRoom(Room(roomId, newName, flat.id))

    fun deleteRoom(room: Room) {
        _refreshing.value = true
        viewModelScope.launch {
            when (val res = useCases.deleteRoom(room)) {
                is Result.Success -> {
                    withContext(Dispatchers.Main) { onRoomDeleted(room) }
                    _rooms.value = _rooms.value?.filter { it.id != room.id }
                }
                is Result.Failure -> onRoomUpdateFailed(res.error)
            }
            _refreshing.value = false
        }
    }
}