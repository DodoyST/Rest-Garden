package com.example.restgarden.data.adapter.viewholder

import android.view.View
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.restgarden.R
import com.example.restgarden.data.model.Booking
import com.example.restgarden.data.viewmodel.BookingViewModel
import com.example.restgarden.databinding.CardBookingBinding
import com.example.restgarden.util.dateFormat

class BookingViewHolder(itemView: View, private val bookingViewModel: BookingViewModel) :
  RecyclerView.ViewHolder(itemView) {
  
  private val binding = CardBookingBinding.bind(itemView)
  var id = ""
  
  fun bind(booking: Booking) {
    id = booking.id
    binding.apply {
      tvCardBookingName.text = booking.graveName
      tvCardBookingDateExpired.text = booking.expiredDate.dateFormat()
      tvCardBookingSlot.text = booking.totalSlot.toString()
    }
  }
  
  init {
    itemView.setOnClickListener {
      itemView.findNavController().navigate(R.id.action_global_bookingDetailFragment)
      bookingViewModel.getById(id)
    }
  }
}
