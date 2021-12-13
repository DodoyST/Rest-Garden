package com.example.restgarden.data.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.restgarden.data.model.Transaction
import com.example.restgarden.databinding.CardTransactionBinding

class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
  
  private val binding = CardTransactionBinding.bind(itemView)
  private var id = ""
  
  fun bind(transaction: Transaction) {
    binding.apply {
      id = transaction.id
      tvCardTransactionName.text = transaction.graveName
      tvCardTransactionAddress.text = transaction.graveAddress
      tvCardTransactionSlot.text = transaction.totalSlot.toString()
    }
  }
}
