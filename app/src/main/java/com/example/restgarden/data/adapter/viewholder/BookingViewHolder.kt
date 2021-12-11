package com.example.restgarden.data.adapter.viewholder

import android.view.View
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.restgarden.R
import com.example.restgarden.data.model.Transaction
import com.example.restgarden.data.viewmodel.TransactionViewModel
import com.example.restgarden.databinding.CardBookingBinding

class BookingViewHolder(itemView: View, private val transactionViewModel: TransactionViewModel) :
  RecyclerView.ViewHolder(itemView) {
  
  private val binding = CardBookingBinding.bind(itemView)
  var id = ""
  
  fun bind(transaction: Transaction) {
    id = transaction.id
    binding.apply {
      tvCardBookingName.text = transaction.graveName
      tvCardBookingAddress.text = transaction.graveAddress
      tvCardBookingSlot.text = transaction.totalSlot.toString()
    }
  }
  
  init {
    itemView.setOnClickListener {
      itemView.findNavController().navigate(R.id.action_global_bookingDetailFragment)
      transactionViewModel.getById(id)
    }
  }
}
