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
import com.example.restgarden.data.model.Grave
import com.example.restgarden.data.repository.GraveRepositoryImpl
import com.example.restgarden.data.repository.TransactionRepositoryImpl
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
  
  private lateinit var homeActivity: HomeActivity
  
  private lateinit var graveViewModel: GraveViewModel
  
  @Inject
  lateinit var graveRepositoryImpl: GraveRepositoryImpl
  
  private lateinit var bookingViewModel: BookingViewModel
  
  @Inject
  lateinit var transactionRepositoryImpl: TransactionRepositoryImpl
  
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
        findNavController().navigate(R.id.action_global_graveDetailFragment)
        formClear()
      }
      btnBookingPlus.setOnClickListener {
        bookingViewModel.increment()
      }
      btnBookingMinus.setOnClickListener {
        bookingViewModel.decrement()
      }
      btnBookingSubmit.setOnClickListener {
        booking()
      }
    }
    
    subscribe()
    getById()
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
        BookingViewModel(transactionRepositoryImpl) as T
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
        is AppResource.Success -> {
          val response = it.data
          if (response != null) getByIdSuccess(it.data)
          isNotLoading()
        }
        is AppResource.Error -> {
          getByIdError()
          isNotLoading()
        }
        is AppResource.Loading -> isLoading()
      }
    })
  }
  
  private fun getByIdSuccess(grave: Grave) {
    binding.apply {
      id = grave.id
      tvBookingName.text = grave.name
      tvBookingAddress.text = grave.address
      tvBookingGravePrice.text = grave.price.currencyFormat()
      bookingViewModel.setPrice(grave.price)
    }
  }
  
  private fun getByIdError() {
    Snackbar.make(requireView(), getString(R.string.something_wrong), Snackbar.LENGTH_SHORT).show()
    findNavController().navigate(R.id.action_global_graveDetailFragment)
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
  
  private fun booking() {
    binding.apply {
      sessionManager.fetchAuthId()?.let {
        bookingViewModel.booking(binding.etBookingNotesValue.text.toString(), id, it)
          .observe(viewLifecycleOwner, {
            when (it) {
              is AppResource.Success -> bookingSuccess()
              is AppResource.Error -> bookingError()
              is AppResource.Loading -> isLoading()
            }
          })
      }
    }
  }
  
  private fun bookingSuccess() {
    Toast.makeText(requireContext(), "Thank you for ordering the grave", Toast.LENGTH_LONG).show()
    isNotLoading()
    findNavController().navigate(R.id.action_global_homeFragment)
    formClear()
  }
  
  private fun bookingError() {
    Snackbar.make(requireView(), getString(R.string.something_wrong), Snackbar.LENGTH_LONG).show()
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
