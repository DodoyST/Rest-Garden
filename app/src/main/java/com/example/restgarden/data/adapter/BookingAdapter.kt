package com.example.restgarden.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.restgarden.R
import com.example.restgarden.data.adapter.viewholder.BookingViewHolder
import com.example.restgarden.data.model.Booking

class BookingAdapter(private val bookingList: List<Booking>) :
  RecyclerView.Adapter<BookingViewHolder>() {
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingViewHolder {
    val view =
      LayoutInflater.from(parent.context).inflate(R.layout.card_booking, parent, false)
    return BookingViewHolder(view)
  }
  
  override fun onBindViewHolder(holder: BookingViewHolder, position: Int) {
    holder.bind(bookingList[position])
  }
  
  override fun getItemCount(): Int = bookingList.size
}
