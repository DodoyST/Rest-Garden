package com.example.restgarden.ui.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.restgarden.data.adapter.BookingAdapter
import com.example.restgarden.data.model.Booking
import com.example.restgarden.data.repository.TransactionRepository
import com.example.restgarden.data.viewmodel.BookingViewModel
import com.example.restgarden.databinding.FragmentBookingListBinding
import com.example.restgarden.util.AppResource
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class BookingListFragment : DaggerFragment() {
  private var _binding: FragmentBookingListBinding? = null
  private val binding get() = _binding!!
  
  private lateinit var bookingViewModel: BookingViewModel
  
  @Inject
  lateinit var transactionRepository: TransactionRepository
  
  private lateinit var bookingAdapter: BookingAdapter
  
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
    bookingViewModel = ViewModelProvider(requireActivity(), object : ViewModelProvider.Factory {
      override fun <T : ViewModel> create(modelClass: Class<T>): T =
        BookingViewModel(transactionRepository) as T
    })[BookingViewModel::class.java]
  }
  
  private fun subscribe() {
    bookingViewModel.getAll().observe(viewLifecycleOwner, {
      when (it) {
        is AppResource.Success -> if (it.data != null) subscribeSuccess(it.data)
        is AppResource.Error -> subscribeError()
        is AppResource.Loading -> isLoading()
      }
    })
  }
  
  private fun subscribeSuccess(bookingList: List<Booking>) {
    bookingAdapter = BookingAdapter(bookingList, bookingViewModel)
    binding.rvBookingList.apply {
      adapter = bookingAdapter
      layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }
    isNotLoading()
  }
  
  private fun subscribeError() {
    Toast.makeText(requireContext(), "Something Wrong...", Toast.LENGTH_LONG).show()
    isNotLoading()
  }
  
  private fun isNotLoading() {
    binding.apply {
      tvBookingList.visibility = View.VISIBLE
      rvBookingList.visibility = View.VISIBLE
      pbBookingList.visibility = View.GONE
    }
  }
  
  private fun isLoading() {
    binding.apply {
      tvBookingList.visibility = View.INVISIBLE
      rvBookingList.visibility = View.INVISIBLE
      pbBookingList.visibility = View.VISIBLE
    }
  }
}
