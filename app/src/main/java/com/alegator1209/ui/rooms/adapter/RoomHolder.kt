package com.alegator1209.ui.rooms.adapter

import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.alegator1209.R
import com.alegator1209.model.Room

class RoomHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val roomName: TextView = view.findViewById(R.id.roomName)
    private val roomId: TextView = view.findViewById(R.id.roomId)
    private val roomEdit: ImageButton = view.findViewById(R.id.roomEdit)
    private val roomDelete: ImageButton = view.findViewById(R.id.roomDelete)

    fun setOnRoomEdit(f: () -> Unit) {
        roomEdit.setOnClickListener { f() }
    }

    fun setOnRoomDelete(f: () -> Unit) {
        roomDelete.setOnClickListener { f() }
    }

    fun bind(room: Room) {
        roomName.text = room.name
        roomId.text = room.id.toString()
    }
}