package com.example.restgarden.data.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.restgarden.data.model.Booking
import com.example.restgarden.databinding.CardBookingBinding

class BookingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
  
  private val binding = CardBookingBinding.bind(itemView)
  var id = ""
  
  fun bind(booking: Booking) {
    id = booking.id
    binding.apply {
      tvCardBookingName.text = booking.graveName
      tvCardBookingAddress.text = booking.graveAddress
      tvCardBookingSlot.text = booking.totalSlot.toString()
    }
  }
}
