package com.example.restgarden.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.restgarden.R
import com.example.restgarden.data.adapter.viewholder.TransactionViewHolder
import com.example.restgarden.data.model.Transaction
import com.example.restgarden.data.viewmodel.TransactionViewModel

class TransactionAdapter(
  private val transactionList: List<Transaction>,
  private val transactionViewModel: TransactionViewModel
) :
  RecyclerView.Adapter<TransactionViewHolder>() {
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.card_transaction, parent, false)
    return TransactionViewHolder(view, transactionViewModel)
  }
  
  override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
    holder.bind(transactionList[position])
  }
  
  override fun getItemCount(): Int = transactionList.size
}
