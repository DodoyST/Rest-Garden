package com.example.restgarden.data.adapter.viewholder

import android.view.View
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.restgarden.R
import com.example.restgarden.data.model.Transaction
import com.example.restgarden.data.viewmodel.TransactionViewModel
import com.example.restgarden.databinding.CardTransactionBinding
import com.example.restgarden.util.currencyFormat

class TransactionViewHolder(
  itemView: View,
  private val transactionViewModel: TransactionViewModel
) : RecyclerView.ViewHolder(itemView) {
  
  private val binding = CardTransactionBinding.bind(itemView)
  private var id = ""
  
  fun bind(transaction: Transaction) {
    binding.apply {
      id = transaction.id
      tvCardTransactionName.text = transaction.graveName
      tvCardTransactionTotalPrice.text = transaction.totalPrice.currencyFormat()
      tvCardTransactionSlot.text = transaction.totalSlot.toString()
    }
  }
  
  init {
    itemView.setOnClickListener {
      itemView.findNavController().navigate(R.id.action_global_transactionDetailFragment)
      transactionViewModel.getById(id)
    }
  }
}
