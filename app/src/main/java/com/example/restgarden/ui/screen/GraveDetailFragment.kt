package com.example.restgarden.ui.screen

import android.annotation.SuppressLint
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
import com.example.restgarden.data.repository.GraveRepositoryImpl
import com.example.restgarden.data.viewmodel.GraveViewModel
import com.example.restgarden.databinding.FragmentGraveDetailBinding
import com.example.restgarden.ui.HomeActivity
import com.example.restgarden.util.AppResource
import com.example.restgarden.util.SessionManager
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class GraveDetailFragment : DaggerFragment() {
  private var _binding: FragmentGraveDetailBinding? = null
  private val binding get() = _binding!!
  
  private lateinit var homeActivity: HomeActivity
  
  private lateinit var graveViewModel: GraveViewModel
  
  @Inject
  lateinit var graveRepositoryImpl: GraveRepositoryImpl
  
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
    _binding = FragmentGraveDetailBinding.inflate(inflater, container, false)
    return binding.root
  }
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    
    homeActivity = activity as HomeActivity
    if (sessionManager.isLoggedIn()) homeActivity.showBnvHome()
    
    graveViewModel = ViewModelProvider(requireActivity(), object : ViewModelProvider.Factory {
      override fun <T : ViewModel> create(modelClass: Class<T>): T =
        GraveViewModel(graveRepositoryImpl) as T
    })[GraveViewModel::class.java]
    
    subscribe()
    
    binding.apply {
      btnGraveDetailBack.setOnClickListener {
        findNavController().navigate(R.id.action_global_homeFragment)
        graveViewModel.clearGrave()
      }
      
      btnGraveDetailBuy.setOnClickListener {
        if (!sessionManager.isLoggedIn()) dialogNeedSignIn()
        else findNavController().navigate(R.id.action_global_buyFragment)
      }
      
      btnGraveDetailBooking.setOnClickListener {
        if (!sessionManager.isLoggedIn()) dialogNeedSignIn()
        else findNavController().navigate(R.id.action_global_bookingFragment)
      }
      
    }
  }
  
  override fun onDestroy() {
    super.onDestroy()
    
    _binding = null
  }
  
  private fun subscribe() {
    graveViewModel.grave.observe(viewLifecycleOwner, {
      when (it) {
        is AppResource.Success -> it.data?.let { it1 -> subscribeSuccess(it1) }
        is AppResource.Error -> it.message?.let { it1 -> subscribeError(it1) }
        is AppResource.Loading -> isLoading()
      }
    })
  }
  
  @SuppressLint("SetTextI18n")
  private fun subscribeSuccess(grave: Grave) {
    binding.apply {
      tvGraveDetailName.text = grave.name
      tvGraveDetailSlot.text = "${grave.availableSlots} Slots"
      tvGraveDetailType.text = grave.type
      tvGraveDetailPhoneNumberValue.text = grave.phoneNumber
      tvGraveDetailAddressValue.text = grave.address
      tvGraveDetailDescriptionValue.text = grave.description
      Picasso.get().load(grave.image).into(ivGraveDetail)
      if (grave.type == "Public") btnGraveDetailBooking.visibility = View.GONE
      else btnGraveDetailBooking.visibility = View.VISIBLE
    }
    isNotLoading()
  }
  
  private fun subscribeError(message: String) {
    Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG).show()
    findNavController().navigate(R.id.action_global_homeFragment)
    isNotLoading()
  }
  
  private fun isNotLoading() {
    binding.apply {
      pbGraveDetail.visibility = View.GONE
      lnlGraveDetail.visibility = View.VISIBLE
    }
  }
  
  private fun isLoading() {
    binding.apply {
      pbGraveDetail.visibility = View.VISIBLE
      lnlGraveDetail.visibility = View.GONE
    }
  }
  
  private fun dialogNeedSignIn() {
    AlertDialog.Builder(requireContext()).setTitle(getString(R.string.sign_in))
      .setMessage("You need login access to book a grave, want to go to the login page?")
      .setNegativeButton(getString(R.string.no)) { dialog, _ ->
        dialog.dismiss()
      }.setPositiveButton(getString(R.string.yes)) { dialog, _ ->
        findNavController().navigate(R.id.action_global_authActivity)
        dialog.dismiss()
      }.show()
  }
}
