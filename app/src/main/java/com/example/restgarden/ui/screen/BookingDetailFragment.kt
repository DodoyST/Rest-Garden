package com.example.restgarden.ui.screen

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.restgarden.R
import com.example.restgarden.data.model.Transaction
import com.example.restgarden.data.repository.TransactionRepository
import com.example.restgarden.data.viewmodel.TransactionViewModel
import com.example.restgarden.databinding.FragmentBookingDetailBinding
import com.example.restgarden.util.AppResource
import com.example.restgarden.util.toDate
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class BookingDetailFragment : DaggerFragment() {
  private var _binding: FragmentBookingDetailBinding? = null
  private val binding get() = _binding!!
  
  private var id = ""
  
  private lateinit var transactionViewModel: TransactionViewModel
  
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
    
    binding.apply {
      btnBookingDetailBack.setOnClickListener {
        clear()
        findNavController().navigate(R.id.action_global_bookingListFragment)
      }
      
      btnBookingDetailCancel.setOnClickListener {
        alertCancel()
      }
    }
    
    subscribe()
  }
  
  override fun onDestroy() {
    super.onDestroy()
    
    _binding = null
    clear()
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
      tvBookingDetailGraveName.text = transaction.graveName
      tvBookingDetailGraveAddress.text = transaction.graveAddress
      tvBookingDetailStatusValue.text = transaction.status
      tvBookingDetailDateExpiredValue.text = transaction.expiredDate.toDate()
      tvBookingDetailReservedSlotsValue.text = transaction.totalSlot.toString()
      if (transaction.description.trim().isNotBlank()) {
        tvBookingDetailNotes.visibility = View.VISIBLE
        tvBookingDetailNotesValue.visibility = View.VISIBLE
        tvBookingDetailNotesValue.text = transaction.description
      } else {
        tvBookingDetailNotes.visibility = View.GONE
        tvBookingDetailNotesValue.visibility = View.GONE
      }
    }
    isNotLoading()
  }
  
  private fun subscribeError(message: String) {
    findNavController().navigate(R.id.action_global_bookingListFragment)
    isNotLoading()
    clear()
    Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
  }
  
  private fun isNotLoading() {
    binding.apply {
      pbBookingDetail.visibility = View.GONE
      ivBookingDetail.visibility = View.VISIBLE
      lnlBookingDetail.visibility = View.VISIBLE
      btnBookingDetailBack.visibility = View.VISIBLE
    }
  }
  
  private fun isLoading() {
    binding.apply {
      pbBookingDetail.visibility = View.VISIBLE
      ivBookingDetail.visibility = View.INVISIBLE
      lnlBookingDetail.visibility = View.INVISIBLE
      btnBookingDetailBack.visibility = View.GONE
    }
  }
  
  private fun clear() {
    id = ""
  }
  
  private fun alertCancel() {
    AlertDialog.Builder(requireContext()).setMessage("Do you want to cancel this order?")
      .setNegativeButton(getString(R.string.no)) { dialog, _ ->
        dialog.dismiss()
      }.setPositiveButton(getString(R.string.yes)) { dialog, _ ->
        dialog.dismiss()
        cancel()
      }.show()
  }
  
  private fun cancel() {
    transactionViewModel.cancelBooking(id).observe(viewLifecycleOwner, {
      when (it) {
        is AppResource.Success -> cancelSuccess()
        is AppResource.Error -> it.message?.let { it1 -> cancelError(it1) }
        is AppResource.Loading -> isLoading()
      }
    })
  }
  
  private fun cancelSuccess() {
    findNavController().navigate(R.id.action_global_homeFragment)
    isNotLoading()
    clear()
  }
  
  private fun cancelError(message: String) {
    Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG).show()
    isNotLoading()
  }
}
