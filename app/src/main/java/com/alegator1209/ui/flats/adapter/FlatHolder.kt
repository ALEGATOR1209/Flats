package com.alegator1209.ui.flats.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.alegator1209.R
import com.alegator1209.model.Flat

class FlatHolder(view: View) : RecyclerView.ViewHolder(view) {
    init {
        view.setOnClickListener(this::onClick)
    }

    private val flatId = view.findViewById<TextView>(R.id.flatId)
    private val flatName = view.findViewById<TextView>(R.id.flatName)
    private val flatAddress = view.findViewById<TextView>(R.id.flatAddress)
    private var onClickListener: () -> Unit = {}

    fun bind(flat: Flat) {
        flatId.text = flat.id.toString()
        flatName.text = flat.name
        flatAddress.text = flat.address
    }

    fun setOnClickListener(f: () -> Unit) { onClickListener = f }

    @Suppress("UNUSED_PARAMETER")
    private fun onClick(view: View) {
        onClickListener()
    }
}
