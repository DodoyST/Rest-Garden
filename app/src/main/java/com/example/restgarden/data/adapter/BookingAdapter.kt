package com.example.restgarden.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.restgarden.R
import com.example.restgarden.data.adapter.viewholder.BookingViewHolder
import com.example.restgarden.data.model.Transaction
import com.example.restgarden.data.viewmodel.TransactionViewModel

class BookingAdapter(
  private val transactionList: List<Transaction>,
  private val transactionViewModel: TransactionViewModel
) :
  RecyclerView.Adapter<BookingViewHolder>() {
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingViewHolder {
    val view =
      LayoutInflater.from(parent.context).inflate(R.layout.card_booking, parent, false)
    return BookingViewHolder(view, transactionViewModel)
  }
  
  override fun onBindViewHolder(holder: BookingViewHolder, position: Int) {
    holder.bind(transactionList[position])
  }
  
  override fun getItemCount(): Int = transactionList.size
}
