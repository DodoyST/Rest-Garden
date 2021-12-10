package com.example.restgarden.ui.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.restgarden.R
import com.example.restgarden.data.model.Booking
import com.example.restgarden.data.repository.TransactionRepository
import com.example.restgarden.data.viewmodel.BookingViewModel
import com.example.restgarden.databinding.FragmentBookingDetailBinding
import com.example.restgarden.util.AppResource
import com.example.restgarden.util.toDate
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class BookingDetailFragment : DaggerFragment() {
  private var _binding: FragmentBookingDetailBinding? = null
  private val binding get() = _binding!!
  
  private var id = ""
  
  private lateinit var bookingViewModel: BookingViewModel
  
  @Inject
  lateinit var transactionRepository: TransactionRepository
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
  }
  
  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    _binding = FragmentBookingDetailBinding.inflate(inflater, container, false)
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
    bookingViewModel.booking.observe(viewLifecycleOwner, {
      when (it) {
        is AppResource.Success -> it.data?.let { it1 -> subscribeSuccess(it1) }
        is AppResource.Error -> it.message?.let { it1 -> subscribeError(it1) }
        is AppResource.Loading -> isLoading()
      }
    })
  }
  
  private fun subscribeSuccess(booking: Booking) {
    id = booking.id
    binding.apply {
      tvBookingDetailGraveName.text = booking.graveName
      tvBookingDetailGraveAddress.text = booking.graveAddress
      tvBookingDetailStatusValue.text = booking.status
      tvBookingDetailDateExpiredValue.text = booking.expiredDate.toDate()
      tvBookingDetailReservedSlotsValue.text = booking.totalSlot.toString()
    }
    isNotLoading()
  }
  
  private fun subscribeError(message: String) {
    findNavController().navigate(R.id.action_global_bookingListFragment)
    isNotLoading()
    Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
  }
  
  private fun isNotLoading() {
    binding.apply {
      pbBookingDetail.visibility = View.GONE
      ivBookingDetail.visibility = View.VISIBLE
      lnlBookingDetail.visibility = View.VISIBLE
    }
  }
  
  private fun isLoading() {
    binding.apply {
      pbBookingDetail.visibility = View.VISIBLE
      ivBookingDetail.visibility = View.INVISIBLE
      lnlBookingDetail.visibility = View.INVISIBLE
    }
  }
  
  private fun clear() {
    id = ""
  }
}
