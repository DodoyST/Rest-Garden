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
import com.example.restgarden.data.repository.GraveRepositoryImpl
import com.example.restgarden.data.viewmodel.GraveViewModel
import com.example.restgarden.databinding.FragmentGraveDetailBinding
import com.example.restgarden.util.AppResource
import com.example.restgarden.util.SessionManager
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class GraveDetailFragment : DaggerFragment() {
  private var _binding: FragmentGraveDetailBinding? = null
  private val binding get() = _binding!!
  
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
    
    graveViewModel = ViewModelProvider(requireActivity(), object : ViewModelProvider.Factory {
      override fun <T : ViewModel> create(modelClass: Class<T>): T =
        GraveViewModel(graveRepositoryImpl) as T
    })[GraveViewModel::class.java]
    
    subscribe()
    
    binding.apply {
      btnGraveDetailBack.setOnClickListener {
        findNavController().navigate(R.id.action_global_graveDetailFragment_to_homeFragment)
        graveViewModel.clearGrave()
      }
      
      btnGraveDetailBooking.setOnClickListener {
        dialogNeedSignIn()
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
        is AppResource.Success -> {
          val response = it.data
          if (response != null) {
            binding.apply {
              tvGraveDetailName.text = response.name
              tvGraveDetailSlot.text = response.availableSlots.toString()
              tvGraveDetailType.text = response.type
              tvGraveDetailPhoneNumberValue.text = response.phoneNumber
              tvGraveDetailAddressValue.text = response.address
              tvGraveDetailDescriptionValue.text = response.description
              if (response.type == "Public") btnGraveDetailBooking.visibility = View.GONE
              else btnGraveDetailBooking.visibility = View.VISIBLE
            }
            isNotLoading()
          }
        }
        is AppResource.Error -> findNavController().navigate(R.id.action_global_graveDetailFragment_to_homeFragment)
        is AppResource.Loading -> isLoading()
      }
    })
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
        findNavController().navigate(R.id.action_global_graveDetailFragment_to_authActivity)
        dialog.dismiss()
      }.show()
  }
}
