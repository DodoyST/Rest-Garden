package com.example.restgarden.ui.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.restgarden.R
import com.example.restgarden.data.model.Transaction
import com.example.restgarden.data.repository.TransactionRepository
import com.example.restgarden.data.viewmodel.TransactionViewModel
import com.example.restgarden.databinding.FragmentTransactionDetailBinding
import com.example.restgarden.util.AppResource
import com.example.restgarden.util.currencyFormat
import com.example.restgarden.util.timestampFormat
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class TransactionDetailFragment : DaggerFragment() {
  private var _binding: FragmentTransactionDetailBinding? = null
  private val binding get() = _binding!!
  
  private lateinit var transactionViewModel: TransactionViewModel
  
  @Inject
  lateinit var transactionRepository: TransactionRepository
  
  private var id = ""
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
  }
  
  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    _binding = FragmentTransactionDetailBinding.inflate(inflater, container, false)
    return binding.root
  }
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    
    instanceViewModel()
    
    binding.apply {
      btnTransactionDetailBack.setOnClickListener {
        findNavController().navigate(R.id.action_global_transactionListFragment)
        clear()
      }
    }
    
    subscribe()
  }
  
  override fun onDestroy() {
    super.onDestroy()
    
    _binding = null
  }
  
  private fun instanceViewModel() {
    transactionViewModel = ViewModelProvider(requireActivity(), object : ViewModelProvider.Factory {
      override fun <T : ViewModel> create(modelClass: Class<T>): T =
        TransactionViewModel(transactionRepository) as T
    })[TransactionViewModel::class.java]
  }
  
  private fun subscribe() {
    transactionViewModel.transaction.observe(viewLifecycleOwner, {
      when (it) {
        is AppResource.Success -> it.data?.let { it1 -> subscribeSuccess(it1) }
        is AppResource.Error -> it.message?.let { it1 -> subscribeError(it1) }
        is AppResource.Loading -> isLoading()
      }
    })
  }
  
  private fun subscribeSuccess(transaction: Transaction) {
    id = transaction.id
    binding.apply {
      tvTransactionDetailGraveName.text = transaction.graveName
      tvTransactionDetailGraveAddress.text = transaction.graveAddress
      tvTransactionDetailDateValue.text = transaction.date.timestampFormat()
      tvTransactionDetailSlotsValue.text = transaction.totalSlot.toString()
      tvTransactionDetailTotalPriceValue.text = transaction.totalPrice.currencyFormat()
      tvTransactionDetailIdValue.text = transaction.id
      Picasso.get().load(transaction.image).into(ivTransactionDetail)
      if (transaction.description.trim().isNotBlank()) {
        tvTransactionDetailNotes.visibility = View.VISIBLE
        tvTransactionDetailNotesValue.visibility = View.VISIBLE
        tvTransactionDetailNotesValue.text = transaction.description
      } else {
        tvTransactionDetailNotes.visibility = View.GONE
        tvTransactionDetailNotesValue.visibility = View.GONE
      }
    }
    isNotLoading()
  }
  
  private fun subscribeError(message: String) {
    Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG).show()
    findNavController().navigate(R.id.action_global_transactionListFragment)
    isNotLoading()
    clear()
  }
  
  private fun isNotLoading() {
    binding.apply {
      pbTransactionDetail.visibility = View.GONE
      ivTransactionDetail.visibility = View.VISIBLE
      lnlTransactionDetail.visibility = View.VISIBLE
      btnTransactionDetailBack.visibility = View.VISIBLE
    }
  }
  
  private fun isLoading() {
    binding.apply {
      pbTransactionDetail.visibility = View.VISIBLE
      ivTransactionDetail.visibility = View.INVISIBLE
      lnlTransactionDetail.visibility = View.INVISIBLE
      btnTransactionDetailBack.visibility = View.GONE
    }
  }
  
  private fun clear() {
    id = ""
  }
}
