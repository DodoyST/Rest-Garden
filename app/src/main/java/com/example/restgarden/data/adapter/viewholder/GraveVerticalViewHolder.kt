package com.example.restgarden.data.adapter.viewholder

import android.view.View
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.restgarden.R
import com.example.restgarden.data.model.Grave
import com.example.restgarden.data.viewmodel.GraveViewModel
import com.example.restgarden.databinding.CardGraveVerticalBinding

class GraveVerticalViewHolder(itemView: View, private val graveViewModel: GraveViewModel) :
  RecyclerView.ViewHolder(itemView) {
  
  val binding = CardGraveVerticalBinding.bind(itemView)
  var id = ""
  
  fun bind(grave: Grave) {
    binding.apply {
      id = grave.id
      tvCardGraveVerticalName.text = grave.name
      tvCardGraveVerticalAddress.text = grave.address
    }
  }
  
  init {
    itemView.setOnClickListener {
      itemView.findNavController().navigate(R.id.action_global_graveDetailFragment)
      graveViewModel.getById(id)
    }
  }
}
