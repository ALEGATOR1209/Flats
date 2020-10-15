package com.alegator1209.ui.rooms.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alegator1209.R
import com.alegator1209.model.Room

class RoomAdapter(private var rooms: List<Room>) : RecyclerView.Adapter<RoomHolder>() {
    private lateinit var onEditRoom: (Room) -> Unit
    private lateinit var onDeleteRoom: (Room) -> Unit

    fun setOnEditRoomListener(f: (Room) -> Unit) { onEditRoom = f }
    fun setOnDeleteRoomListener(f: (Room) -> Unit) { onDeleteRoom = f }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_room, parent, false)
        return RoomHolder(view)
    }

    override fun onBindViewHolder(holder: RoomHolder, position: Int) {
        val room = rooms[position]
        holder.bind(room)
        holder.setOnRoomEdit { onEditRoom(room) }
        holder.setOnRoomDelete { onDeleteRoom(room) }
    }

    override fun getItemCount() = rooms.size

    fun update(newRooms: List<Room>) {
        rooms = newRooms
        notifyDataSetChanged()
    }

    fun updateRoom(updated: Room) {
        val index = rooms.indexOfFirst { it.id == updated.id }
        notifyItemChanged(index)
    }

    fun deleteRoom(room: Room) {
        val index = rooms.indexOfFirst { it.id == room.id }
        notifyItemRemoved(index)
    }
}