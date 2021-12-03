package com.example.restgarden.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.restgarden.R
import com.example.restgarden.data.adapter.viewholder.GraveHorizontalViewHolder
import com.example.restgarden.data.model.Grave

class GraveHorizontalAdapter(private val graveList: List<Grave>) :
  RecyclerView.Adapter<GraveHorizontalViewHolder>() {
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GraveHorizontalViewHolder {
    val view =
      LayoutInflater.from(parent.context).inflate(R.layout.card_grave_horizontal, parent, false)
    return GraveHorizontalViewHolder(view)
  }
  
  override fun onBindViewHolder(holder: GraveHorizontalViewHolder, position: Int) {
    holder.bind(graveList[position])
  }
  
  override fun getItemCount(): Int = graveList.size
}
