package com.example.restgarden.data.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.restgarden.data.model.Grave
import com.example.restgarden.databinding.CardGraveHorizontalBinding

class GraveHorizontalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
  val binding = CardGraveHorizontalBinding.bind(itemView)
  
  fun bind(grave: Grave) {
    binding.apply {
      tvCardGraveHorizpntalName.text = grave.name
      tvCardGraveHorizontalAddress.text = grave.address
    }
  }
}
