package com.example.restgarden.ui.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.restgarden.R
import com.example.restgarden.data.adapter.BookingAdapter
import com.example.restgarden.data.model.Transaction
import com.example.restgarden.data.repository.TransactionRepository
import com.example.restgarden.data.viewmodel.TransactionViewModel
import com.example.restgarden.databinding.FragmentBookingListBinding
import com.example.restgarden.util.AppResource
import com.example.restgarden.util.SessionManager
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class BookingListFragment : DaggerFragment() {
  private var _binding: FragmentBookingListBinding? = null
  private val binding get() = _binding!!
  
  private lateinit var transactionViewModel: TransactionViewModel
  
  @Inject
  lateinit var transactionRepository: TransactionRepository
  
  private lateinit var bookingAdapter: BookingAdapter
  
  @Inject
  lateinit var sessionManager: SessionManager
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
  }
  
  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    _binding = FragmentBookingListBinding.inflate(inflater, container, false)
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
    sessionManager.fetchAuthId()?.let { userId ->
      transactionViewModel.getAll(userId).observe(viewLifecycleOwner, {
        when (it) {
          is AppResource.Success -> if (it.data != null) subscribeSuccess(it.data)
          is AppResource.Error -> it.message?.let { it1 -> subscribeError(it1) }
          is AppResource.Loading -> isLoading()
        }
      })
    }
  }
  
  private fun subscribeSuccess(transactionList: List<Transaction>) {
    bookingAdapter = BookingAdapter(transactionList, transactionViewModel)
    binding.rvBookingList.apply {
      adapter = bookingAdapter
      layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }
    isNotLoading()
  }
  
  private fun subscribeError(message: String) {
    Toast.makeText(requireContext(), getString(R.string.something_wrong), Toast.LENGTH_LONG).show()
    isNotLoading()
  }
  
  private fun isNotLoading() {
    binding.apply {
      rvBookingList.visibility = View.VISIBLE
      pbBookingList.visibility = View.GONE
    }
  }
  
  private fun isLoading() {
    binding.apply {
      rvBookingList.visibility = View.INVISIBLE
      pbBookingList.visibility = View.VISIBLE
    }
  }
}
