package com.example.restgarden.ui.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.restgarden.R
import com.example.restgarden.data.repository.TransactionRepository
import com.example.restgarden.data.viewmodel.TransactionViewModel
import com.example.restgarden.databinding.FragmentReSubscribeBinding
import com.example.restgarden.ui.HomeActivity
import com.example.restgarden.util.AppResource
import com.example.restgarden.util.currencyFormat
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

class ReSubscribeFragment : Fragment() {
  private var _binding: FragmentReSubscribeBinding? = null
  private val binding get() = _binding!!
  
  private lateinit var homeActivity: HomeActivity
  
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
    _binding = FragmentReSubscribeBinding.inflate(inflater, container, false)
    return binding.root
  }
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    
    homeActivity = activity as HomeActivity
    homeActivity.hideBnvHome()
    
    instanceViewModel()
    
    binding.apply {
      btnReSubscribeBack.setOnClickListener {
        findNavController().navigate(R.id.action_global_bookingDetailFragment)
        formClear()
      }
      btnReSubscribeSubmit.setOnClickListener {
        reSubscribe()
      }
      rdgReSubscribePaymentMethod.setOnCheckedChangeListener { _, checkedId ->
        btnReSubscribeSubmit.isEnabled = checkedId != -1
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
    binding.apply {
      transactionViewModel.transaction.observe(viewLifecycleOwner, {
        val data = it.data
        if (data != null) {
          id = data.id
          tvReSubscribeName.text = data.graveName
          tvReSubscribeAddress.text = data.graveAddress
          tvReSubscribeGravePrice.text = data.gravePrice.currencyFormat()
          tvReSubscribeFeeValue.text = data.gravePrice.times(0.2).currencyFormat()
          tvReSubscribeTotalPaymentValue.text = data.gravePrice.times(0.2).currencyFormat()
          isNotLoading()
        }
      })
    }
  }
  
  private fun reSubscribe() {
    transactionViewModel.reSubscribe(id).observe(viewLifecycleOwner, {
      when (it) {
        is AppResource.Success -> reSubscribeSuccess()
        is AppResource.Error -> it.message?.let { it1 -> reSubscribeError(it1) }
        is AppResource.Loading -> isLoading()
      }
    })
  }
  
  private fun reSubscribeSuccess() {
    findNavController().navigate(R.id.action_global_thankFragment)
    formClear()
    homeActivity.showBnvHome()
    isNotLoading()
  }
  
  private fun reSubscribeError(message: String) {
    Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG).show()
    isNotLoading()
  }
  
  private fun isNotLoading() {
    binding.apply {
      pbReSubscribe.visibility = View.GONE
      cslReSubscribe.visibility = View.VISIBLE
      btnReSubscribeBack.visibility = View.VISIBLE
      tvReSubscribeName.visibility = View.VISIBLE
      tvReSubscribeAddress.visibility = View.VISIBLE
    }
  }
  
  private fun isLoading() {
    binding.apply {
      pbReSubscribe.visibility = View.VISIBLE
      cslReSubscribe.visibility = View.GONE
      btnReSubscribeBack.visibility = View.GONE
      tvReSubscribeName.visibility = View.GONE
      tvReSubscribeAddress.visibility = View.GONE
    }
  }
  
  private fun formClear() {
    id = ""
    binding.apply {
      rdgReSubscribePaymentMethod.clearCheck()
    }
    transactionViewModel.liveDataReset()
  }
}
