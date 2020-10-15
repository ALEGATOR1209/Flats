package com.alegator1209.ui.flats.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alegator1209.R
import com.alegator1209.model.Flat

class FlatsAdapter(private var flats: List<Flat>) : RecyclerView.Adapter<FlatHolder>() {
    private lateinit var onFlatClick: (Flat) -> Unit
    fun setOnFlatClickListener(f: (Flat) -> Unit) { onFlatClick = f }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlatHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_flat, parent, false)
        return FlatHolder(view)
    }

    override fun getItemCount() = flats.size

    override fun onBindViewHolder(holder: FlatHolder, position: Int) {
        val flat = flats[position]
        holder.bind(flat)
        holder.setOnClickListener { onFlatClick(flat) }
    }

    fun changeList(newList: List<Flat>) {
        flats = newList
        notifyDataSetChanged()
    }
}