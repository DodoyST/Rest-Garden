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
import com.example.restgarden.data.model.User
import com.example.restgarden.data.repository.UserRepositoryImpl
import com.example.restgarden.data.viewmodel.UserViewModel
import com.example.restgarden.databinding.FragmentProfileBinding
import com.example.restgarden.util.AppResource
import com.example.restgarden.util.SessionManager
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class ProfileFragment : DaggerFragment() {
  private var _binding: FragmentProfileBinding? = null
  private val binding get() = _binding!!
  
  private lateinit var userViewModel: UserViewModel
  
  @Inject
  lateinit var userRepositoryImpl: UserRepositoryImpl
  
  @Inject
  lateinit var sessionManager: SessionManager
  
  private var id = ""
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
  }
  
  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    _binding = FragmentProfileBinding.inflate(inflater, container, false)
    return binding.root
  }
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    
    instanceViewModel()
    
    binding.apply {
      btnProfileLogout.setOnClickListener {
        alertSignOut()
      }
      
      btnProfileEditProfile.setOnClickListener {
        findNavController().navigate(R.id.action_global_userFormFragment)
      }
    }
    
    subscribe()
  }
  
  override fun onDestroy() {
    super.onDestroy()
    
    _binding = null
  }
  
  private fun instanceViewModel() {
    userViewModel = ViewModelProvider(requireActivity(), object : ViewModelProvider.Factory {
      override fun <T : ViewModel> create(modelClass: Class<T>): T =
        UserViewModel(userRepositoryImpl) as T
    })[UserViewModel::class.java]
  }
  
  private fun alertSignOut() {
    AlertDialog.Builder(requireContext()).setMessage(getString(R.string.ask_logout))
      .setNegativeButton(getString(R.string.no)) { dialog, _ ->
        dialog.dismiss()
      }.setPositiveButton(getString(R.string.yes)) { dialog, _ ->
        dialog.dismiss()
        signOut()
      }.show()
  }
  
  private fun signOut() {
    sessionManager.clearPref()
    requireActivity().finish()
    requireActivity().startActivity(requireActivity().intent)
  }
  
  private fun subscribe() {
    userViewModel.user.observe(viewLifecycleOwner, {
      when (it) {
        is AppResource.Success -> it.data?.let { it1 -> subscribeSuccess(it1) }
        is AppResource.Error -> it.message?.let { it1 -> subscribeError(it1) }
        is AppResource.Loading -> isLoading()
      }
    })
  }
  
  private fun subscribeSuccess(user: User) {
    binding.apply {
      id = user.id
      tvProfileName.text = user.name
      tvProfileEmail.text = user.email
      tvProfilePhoneNumberValue.text = user.phoneNumber
      tvProfileAddressValue.text = user.address
    }
    isNotLoading()
  }
  
  private fun subscribeError(message: String) {
    Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG).show()
    signOut()
    isNotLoading()
  }
  
  private fun isNotLoading() {
    binding.apply {
      tvProfilePhoneNumberValue.visibility = View.VISIBLE
      tvProfileAddressValue.visibility = View.VISIBLE
      pbProfile.visibility = View.GONE
      btnProfileLogout.visibility = View.VISIBLE
      btnProfileEditProfile.visibility = View.VISIBLE
    }
  }
  
  private fun isLoading() {
    binding.apply {
      tvProfilePhoneNumberValue.visibility = View.INVISIBLE
      tvProfileAddressValue.visibility = View.INVISIBLE
      pbProfile.visibility = View.VISIBLE
      btnProfileLogout.visibility = View.INVISIBLE
      btnProfileEditProfile.visibility = View.INVISIBLE
    }
  }
}
