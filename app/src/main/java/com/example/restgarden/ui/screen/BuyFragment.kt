package com.example.restgarden.ui.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.restgarden.R
import com.example.restgarden.data.model.Grave
import com.example.restgarden.data.repository.GraveRepositoryImpl
import com.example.restgarden.data.repository.TransactionRepositoryImpl
import com.example.restgarden.data.viewmodel.GraveViewModel
import com.example.restgarden.data.viewmodel.TransactionViewModel
import com.example.restgarden.databinding.FragmentBuyBinding
import com.example.restgarden.ui.HomeActivity
import com.example.restgarden.util.AppResource
import com.example.restgarden.util.SessionManager
import com.example.restgarden.util.currencyFormat
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class BuyFragment : DaggerFragment() {
  private var _binding: FragmentBuyBinding? = null
  private val binding get() = _binding!!
  
  private var id = ""
  private var slot = 0
  
  private lateinit var homeActivity: HomeActivity
  
  private lateinit var graveViewModel: GraveViewModel
  
  @Inject
  lateinit var graveRepositoryImpl: GraveRepositoryImpl
  
  private lateinit var transactionViewModel: TransactionViewModel
  
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
    _binding = FragmentBuyBinding.inflate(inflater, container, false)
    return binding.root
  }
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    
    homeActivity = activity as HomeActivity
    homeActivity.hideBnvHome()
    
    instanceViewModel()
    
    binding.apply {
      btnBuyBack.setOnClickListener {
        findNavController().navigate(R.id.action_buyFragment_to_graveDetailFragment)
        formClear()
      }
      btnBuyPlus.setOnClickListener {
        transactionViewModel.increment(slot)
      }
      btnBuyMinus.setOnClickListener {
        transactionViewModel.decrement()
      }
      btnBuySubmit.setOnClickListener {
        buy()
      }
      rdgBuyPaymentMethod.setOnCheckedChangeListener { _, checkedId ->
        btnBuySubmit.isEnabled = checkedId != -1
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
    
    transactionViewModel = ViewModelProvider(requireActivity(), object : ViewModelProvider.Factory {
      override fun <T : ViewModel> create(modelClass: Class<T>): T =
        TransactionViewModel(transactionRepositoryImpl) as T
    })[TransactionViewModel::class.java]
  }
  
  private fun subscribe() {
    binding.apply {
      transactionViewModel.apply {
        amount.observe(viewLifecycleOwner, {
          tvBuyAmount.text = it.toString()
          btnBuySubmit.visibility = if (it < 1) View.GONE else View.VISIBLE
        })
        totalPrice.observe(viewLifecycleOwner, {
          tvBuyPriceValue.text = it?.currencyFormat()
          tvBuyTotalPaymentValue.text = it?.currencyFormat()
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
      tvBuyName.text = grave.name
      tvBuyAddress.text = grave.address
      tvBuyGravePrice.text = grave.price.currencyFormat()
      transactionViewModel.setPrice(grave.price)
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
      pbBuy.visibility = View.GONE
      cslBuy.visibility = View.VISIBLE
      btnBuyBack.visibility = View.VISIBLE
      tvBuyName.visibility = View.VISIBLE
      tvBuyAddress.visibility = View.VISIBLE
    }
  }
  
  private fun isLoading() {
    binding.apply {
      pbBuy.visibility = View.VISIBLE
      cslBuy.visibility = View.GONE
      btnBuyBack.visibility = View.GONE
      tvBuyName.visibility = View.GONE
      tvBuyAddress.visibility = View.GONE
    }
  }
  
  private fun buy() {
    binding.apply {
      sessionManager.fetchAuthId()?.let { userId ->
        transactionViewModel.buy(binding.etBuyNotesValue.text.toString(), id, userId)
          .observe(viewLifecycleOwner, {
            when (it) {
              is AppResource.Success -> buySuccess()
              is AppResource.Error -> it.message?.let { it1 -> buyError(it1) }
              is AppResource.Loading -> isLoading()
            }
          })
      }
    }
  }
  
  private fun buySuccess() {
    homeActivity.showBnvHome()
    findNavController().navigate(R.id.action_global_thankFragment)
    isNotLoading()
    formClear()
  }
  
  private fun buyError(message: String) {
    Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG).show()
    isNotLoading()
  }
  
  private fun formClear() {
    binding.apply {
      etBuyNotesValue.text?.clear()
      rdgBuyPaymentMethod.clearCheck()
      
    }
    transactionViewModel.liveDataReset()
  }
}
