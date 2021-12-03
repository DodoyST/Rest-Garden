package com.example.restgarden.data.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.restgarden.data.model.Grave
import com.example.restgarden.databinding.CardGraveVerticalBinding

class GraveVerticalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
  val binding = CardGraveVerticalBinding.bind(itemView)
  
  fun bind(grave: Grave) {
    binding.apply {
      tvCardGraveVerticalName.text = grave.name
      tvCardGraveVerticalAddress.text = grave.address
    }
  }
}
