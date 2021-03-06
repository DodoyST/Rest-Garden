package com.example.restgarden.ui.screen

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.restgarden.R
import com.example.restgarden.data.model.Grave
import com.example.restgarden.data.repository.BookingRepository
import com.example.restgarden.data.repository.GraveRepositoryImpl
import com.example.restgarden.data.viewmodel.BookingViewModel
import com.example.restgarden.data.viewmodel.GraveViewModel
import com.example.restgarden.databinding.FragmentBookingBinding
import com.example.restgarden.ui.HomeActivity
import com.example.restgarden.util.AppResource
import com.example.restgarden.util.SessionManager
import com.example.restgarden.util.currencyFormat
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class BookingFragment : DaggerFragment() {
  private var _binding: FragmentBookingBinding? = null
  private val binding get() = _binding!!
  
  private var id = ""
  private var slot = 0
  
  private lateinit var homeActivity: HomeActivity
  
  private lateinit var graveViewModel: GraveViewModel
  
  @Inject
  lateinit var graveRepositoryImpl: GraveRepositoryImpl
  
  private lateinit var bookingViewModel: BookingViewModel
  
  @Inject
  lateinit var bookingRepository: BookingRepository
  
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
    _binding = FragmentBookingBinding.inflate(inflater, container, false)
    return binding.root
  }
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    
    homeActivity = activity as HomeActivity
    homeActivity.hideBnvHome()
    
    instanceViewModel()
    
    binding.apply {
      btnBookingBack.setOnClickListener {
        findNavController().navigate(R.id.action_bookingFragment_to_graveDetailFragment)
        formClear()
      }
      btnBookingPlus.setOnClickListener {
        bookingViewModel.increment(slot)
      }
      btnBookingMinus.setOnClickListener {
        bookingViewModel.decrement()
      }
      btnBookingSubmit.setOnClickListener {
        alertBooking()
      }
      rdgBookingPaymentMethod.setOnCheckedChangeListener { _, checkedId ->
        btnBookingSubmit.isEnabled = checkedId != -1
      }
    }
    
    subscribe()
    getById()
  }
  
  override fun onResume() {
    super.onResume()
    
    homeActivity.hideBnvHome()
  }
  
  override fun onDestroy() {
    super.onDestroy()
    
    _binding = null
  }
  
  private fun instanceViewModel() {
    graveViewModel = ViewModelProvider(requireActivity(), object : ViewModelProvider.Factory {
      override fun <T : ViewModel> create(modelClass: Class<T>): T =
        GraveViewModel(graveRepositoryImpl) as T
    })[GraveViewModel::class.java]
    
    bookingViewModel = ViewModelProvider(requireActivity(), object : ViewModelProvider.Factory {
      override fun <T : ViewModel> create(modelClass: Class<T>): T =
        BookingViewModel(bookingRepository) as T
    })[BookingViewModel::class.java]
  }
  
  private fun subscribe() {
    binding.apply {
      bookingViewModel.apply {
        amount.observe(viewLifecycleOwner, {
          tvBookingAmount.text = it.toString()
          btnBookingSubmit.visibility = if (it < 1) View.GONE else View.VISIBLE
        })
        totalPrice.observe(viewLifecycleOwner, {
          tvBookingPriceValue.text = it?.currencyFormat()
          tvBookingFeeValue.text = it?.times(0.2)?.currencyFormat()
          tvBookingTotalPaymentValue.text = it?.times(1.2)?.currencyFormat()
        })
      }
    }
  }
  
  private fun getById() {
    graveViewModel.grave.observe(viewLifecycleOwner, {
      when (it) {
        is AppResource.Success -> it.data?.let { it1 -> getByIdSuccess(it1) }
        is AppResource.Error -> it.message?.let { it1 -> getByIdError(it1) }
        is AppResource.Loading -> isLoading()
      }
    })
  }
  
  private fun getByIdSuccess(grave: Grave) {
    binding.apply {
      id = grave.id
      slot = grave.availableSlots
      tvBookingName.text = grave.name
      tvBookingAddress.text = grave.address
      tvBookingGravePrice.text = grave.price.currencyFormat()
      bookingViewModel.setPrice(grave.price)
    }
    isNotLoading()
  }
  
  private fun getByIdError(message: String) {
    Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG).show()
    findNavController().navigate(R.id.action_global_graveDetailFragment)
    isNotLoading()
  }
  
  private fun isNotLoading() {
    binding.apply {
      pbBooking.visibility = View.GONE
      cslBooking.visibility = View.VISIBLE
      btnBookingBack.visibility = View.VISIBLE
      tvBookingName.visibility = View.VISIBLE
      tvBookingAddress.visibility = View.VISIBLE
    }
  }
  
  private fun isLoading() {
    binding.apply {
      pbBooking.visibility = View.VISIBLE
      cslBooking.visibility = View.GONE
      btnBookingBack.visibility = View.GONE
      tvBookingName.visibility = View.GONE
      tvBookingAddress.visibility = View.GONE
    }
  }
  
  private fun alertBooking() {
    AlertDialog.Builder(requireContext())
      .setMessage("Are you sure you want to order this grave?")
      .setNegativeButton(getString(R.string.no)) { dialog, _ ->
        dialog.dismiss()
      }.setPositiveButton(getString(R.string.yes)) { dialog, _ ->
        booking()
        dialog.dismiss()
      }.show()
  }
  
  private fun booking() {
    binding.apply {
      sessionManager.fetchAuthId()?.let { userId ->
        bookingViewModel.booking(binding.etBookingNotesValue.text.toString(), id, userId)
          .observe(viewLifecycleOwner, {
            when (it) {
              is AppResource.Success -> bookingSuccess()
              is AppResource.Error -> it.message?.let { it1 -> bookingError(it1) }
              is AppResource.Loading -> isLoading()
            }
          })
      }
    }
  }
  
  private fun bookingSuccess() {
    findNavController().navigate(R.id.action_global_thankFragment)
    formClear()
    isNotLoading()
    homeActivity.showBnvHome()
  }
  
  private fun bookingError(message: String) {
    Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG).show()
    isNotLoading()
  }
  
  private fun formClear() {
    binding.apply {
      etBookingNotesValue.text?.clear()
      rdgBookingPaymentMethod.clearCheck()
    }
    bookingViewModel.liveDataReset()
  }
}
