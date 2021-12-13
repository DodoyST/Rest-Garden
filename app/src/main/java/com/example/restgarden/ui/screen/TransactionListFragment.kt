package com.example.restgarden.ui.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.restgarden.data.adapter.TransactionAdapter
import com.example.restgarden.data.model.Transaction
import com.example.restgarden.data.repository.TransactionRepository
import com.example.restgarden.data.viewmodel.TransactionViewModel
import com.example.restgarden.databinding.FragmentTransactionListBinding
import com.example.restgarden.util.AppResource
import com.example.restgarden.util.SessionManager
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class TransactionListFragment : DaggerFragment() {
  private var _binding: FragmentTransactionListBinding? = null
  private val binding get() = _binding!!
  
  private lateinit var transactionViewModel: TransactionViewModel
  
  @Inject
  lateinit var transactionRepository: TransactionRepository
  
  @Inject
  lateinit var sessionManager: SessionManager
  
  private lateinit var transactionAdapter: TransactionAdapter
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
  }
  
  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    _binding = FragmentTransactionListBinding.inflate(inflater, container, false)
    return binding.root
  }
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    
    instanceViewModel()
    
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
    sessionManager.fetchAuthId()?.let { id ->
      transactionViewModel.getAll(id).observe(viewLifecycleOwner, {
        when (it) {
          is AppResource.Success -> it.data?.let { it1 -> subscribeSuccess(it1) }
          is AppResource.Error -> it.message?.let { it1 -> subscribeError(it1) }
          is AppResource.Loading -> isLoading()
        }
      })
    }
  }
  
  private fun subscribeSuccess(transactionList: List<Transaction>) {
    transactionAdapter = TransactionAdapter(transactionList, transactionViewModel)
    binding.rvTransactionList.apply {
      adapter = transactionAdapter
      layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }
    isNotLoading()
  }
  
  private fun subscribeError(message: String) {
    Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG).show()
    isNotLoading()
  }
  
  private fun isNotLoading() {
    binding.apply {
      rvTransactionList.visibility = View.VISIBLE
      pbTransactionList.visibility = View.GONE
    }
  }
  
  private fun isLoading() {
    binding.apply {
      rvTransactionList.visibility = View.INVISIBLE
      pbTransactionList.visibility = View.VISIBLE
    }
  }
}
