package com.example.restgarden.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.restgarden.R
import com.example.restgarden.data.adapter.viewholder.GraveHorizontalViewHolder
import com.example.restgarden.data.model.Grave
import com.example.restgarden.data.viewmodel.GraveViewModel

class GraveHorizontalAdapter(
  private val graveList: List<Grave>,
  private val graveViewModel: GraveViewModel
) :
  RecyclerView.Adapter<GraveHorizontalViewHolder>() {
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GraveHorizontalViewHolder {
    val view =
      LayoutInflater.from(parent.context).inflate(R.layout.card_grave_horizontal, parent, false)
    return GraveHorizontalViewHolder(view, graveViewModel)
  }
  
  override fun onBindViewHolder(holderHorizontal: GraveHorizontalViewHolder, position: Int) {
    holderHorizontal.bind(graveList[position])
  }
  
  override fun getItemCount(): Int = graveList.size
}
