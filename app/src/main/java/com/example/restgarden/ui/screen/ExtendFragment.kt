package com.example.restgarden.ui.screen

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.restgarden.R
import com.example.restgarden.data.repository.BookingRepository
import com.example.restgarden.data.viewmodel.BookingViewModel
import com.example.restgarden.databinding.FragmentExtendBinding
import com.example.restgarden.ui.HomeActivity
import com.example.restgarden.util.AppResource
import com.example.restgarden.util.currencyFormat
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

class ExtendFragment : Fragment() {
  private var _binding: FragmentExtendBinding? = null
  private val binding get() = _binding!!
  
  private lateinit var homeActivity: HomeActivity
  
  private var id = ""
  
  private lateinit var bookingViewModel: BookingViewModel
  
  @Inject
  lateinit var bookingRepository: BookingRepository
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
  }
  
  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    _binding = FragmentExtendBinding.inflate(inflater, container, false)
    return binding.root
  }
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    
    homeActivity = activity as HomeActivity
    homeActivity.hideBnvHome()
    
    instanceViewModel()
    
    binding.apply {
      btnExtendBack.setOnClickListener {
        findNavController().navigate(R.id.action_global_bookingDetailFragment)
        formClear()
      }
      btnExtendSubmit.setOnClickListener {
        alertExtend()
      }
      rdgExtendPaymentMethod.setOnCheckedChangeListener { _, checkedId ->
        btnExtendSubmit.isEnabled = checkedId != -1
      }
    }
    
    subscribe()
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
    bookingViewModel = ViewModelProvider(requireActivity(), object : ViewModelProvider.Factory {
      override fun <T : ViewModel> create(modelClass: Class<T>): T =
        BookingViewModel(bookingRepository) as T
    })[BookingViewModel::class.java]
  }
  
  private fun subscribe() {
    binding.apply {
      bookingViewModel.booking.observe(viewLifecycleOwner, {
        val data = it.data
        if (data != null) {
          id = data.id
          tvExtendName.text = data.graveName
          tvExtendAddress.text = data.graveAddress
          tvExtendReservedSlotsValue.text = data.totalSlot.toString()
          tvExtendGravePrice.text = data.gravePrice.currencyFormat()
          tvExtendFeeValue.text = data.gravePrice.times(data.totalSlot).times(0.2).currencyFormat()
          tvExtendTotalPaymentValue.text =
            data.gravePrice.times(data.totalSlot).times(0.2).currencyFormat()
          isNotLoading()
        }
      })
    }
  }
  
  private fun alertExtend() {
    AlertDialog.Builder(requireContext())
      .setMessage("Are you sure you want to extend it for another month?")
      .setNegativeButton(getString(R.string.no)) { dialog, _ ->
        dialog.dismiss()
      }.setPositiveButton(getString(R.string.yes)) { dialog, _ ->
        extend()
        dialog.dismiss()
      }.show()
  }
  
  private fun extend() {
    bookingViewModel.extend(id).observe(viewLifecycleOwner, {
      when (it) {
        is AppResource.Success -> extendSuccess()
        is AppResource.Error -> it.message?.let { it1 -> extendError(it1) }
        is AppResource.Loading -> isLoading()
      }
    })
  }
  
  private fun extendSuccess() {
    findNavController().navigate(R.id.action_global_thankFragment)
    formClear()
    homeActivity.showBnvHome()
    isNotLoading()
  }
  
  private fun extendError(message: String) {
    Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG).show()
    isNotLoading()
  }
  
  private fun isNotLoading() {
    binding.apply {
      pbExtend.visibility = View.GONE
      cslExtend.visibility = View.VISIBLE
      btnExtendBack.visibility = View.VISIBLE
      tvExtendName.visibility = View.VISIBLE
      tvExtendAddress.visibility = View.VISIBLE
    }
  }
  
  private fun isLoading() {
    binding.apply {
      pbExtend.visibility = View.VISIBLE
      cslExtend.visibility = View.GONE
      btnExtendBack.visibility = View.GONE
      tvExtendName.visibility = View.GONE
      tvExtendAddress.visibility = View.GONE
    }
  }
  
  private fun formClear() {
    id = ""
    binding.apply {
      rdgExtendPaymentMethod.clearCheck()
    }
    bookingViewModel.liveDataReset()
  }
}
