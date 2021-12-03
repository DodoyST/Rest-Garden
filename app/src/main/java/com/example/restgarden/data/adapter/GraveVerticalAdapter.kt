package com.example.restgarden.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.restgarden.R
import com.example.restgarden.data.adapter.viewholder.GraveVerticalViewHolder
import com.example.restgarden.data.model.Grave

class GraveVerticalAdapter(private val graveList: List<Grave>) :
  RecyclerView.Adapter<GraveVerticalViewHolder>() {
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GraveVerticalViewHolder {
    val view =
      LayoutInflater.from(parent.context).inflate(R.layout.card_grave_vertical, parent, false)
    return GraveVerticalViewHolder(view)
  }
  
  override fun onBindViewHolder(holderHorizontal: GraveVerticalViewHolder, position: Int) {
    holderHorizontal.bind(graveList[position])
  }
  
  override fun getItemCount(): Int = graveList.size
}
