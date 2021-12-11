package com.example.restgarden.ui.screen

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.restgarden.R
import com.example.restgarden.data.model.request.UserRequest
import com.example.restgarden.data.repository.UserRepositoryImpl
import com.example.restgarden.data.viewmodel.UserViewModel
import com.example.restgarden.databinding.FragmentRegisterBinding
import com.example.restgarden.util.AppResource
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class RegisterFragment : DaggerFragment() {
  private var _binding: FragmentRegisterBinding? = null
  private val binding get() = _binding!!
  
  private lateinit var userViewModel: UserViewModel
  
  @Inject
  lateinit var userRepositoryImpl: UserRepositoryImpl
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
  }
  
  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    _binding = FragmentRegisterBinding.inflate(inflater, container, false)
    return binding.root
  }
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    
    instanceViewModel()
    
    formValidation()
    
    binding.apply {
      
      btnRegisterSubmit.setOnClickListener {
        register()
      }
      
      btnRegisterSignIn.setOnClickListener {
        findNavController().navigate(R.id.action_global_signInFragment)
      }
    }
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
  
  private fun instanceUserRequest(): UserRequest {
    binding.apply {
      return UserRequest(
        etRegisterFullName.text.toString(),
        etRegisterUsername.text.toString(),
        etRegisterPassword.text.toString(),
        etRegisterEmail.text.toString(),
        etRegisterPhoneNumber.text.toString(),
        etRegisterAddress.text.toString()
      )
    }
  }
  
  private fun register() {
    userViewModel.register(instanceUserRequest()).observe(viewLifecycleOwner, {
      when (it) {
        is AppResource.Success -> registerSuccess()
        is AppResource.Error -> registerError()
        is AppResource.Loading -> isLoading()
      }
    })
  }
  
  private fun registerSuccess() {
    findNavController().navigate(R.id.action_global_signInFragment)
    isNotLoading()
    formClear()
  }
  
  private fun registerError() {
    Snackbar.make(requireView(), getString(R.string.something_wrong), Snackbar.LENGTH_LONG).show()
    isNotLoading()
  }
  
  private fun isNotLoading() {
    binding.apply {
      etRegisterUsername.isEnabled = true
      etRegisterPassword.isEnabled = true
      etRegisterFullName.isEnabled = true
      etRegisterAddress.isEnabled = true
      etRegisterEmail.isEnabled = true
      etRegisterPhoneNumber.isEnabled = true
      btnRegisterSubmit.isEnabled = true
      btnRegisterSubmit.visibility = View.VISIBLE
      tvRegisterAsk.visibility = View.VISIBLE
      btnRegisterSignIn.isEnabled = true
      btnRegisterSignIn.visibility = View.VISIBLE
    }
  }
  
  private fun isLoading() {
    binding.apply {
      etRegisterUsername.isEnabled = false
      etRegisterPassword.isEnabled = false
      etRegisterFullName.isEnabled = false
      etRegisterAddress.isEnabled = false
      etRegisterEmail.isEnabled = false
      etRegisterPhoneNumber.isEnabled = false
      btnRegisterSubmit.isEnabled = false
      btnRegisterSubmit.visibility = View.INVISIBLE
      tvRegisterAsk.visibility = View.INVISIBLE
      btnRegisterSignIn.isEnabled = false
      btnRegisterSignIn.visibility = View.INVISIBLE
    }
  }
  
  private fun formValidation() {
    binding.apply {
      etRegisterEmail.setOnFocusChangeListener { _, _ ->
        btnRegisterSubmit.isEnabled =
          Patterns.EMAIL_ADDRESS.matcher(etRegisterEmail.text.toString()).matches()
      }
    }
  }
  
  private fun formClear() {
    binding.apply {
      etRegisterUsername.text?.clear()
      etRegisterPassword.text?.clear()
      etRegisterFullName.text?.clear()
      etRegisterAddress.text?.clear()
      etRegisterEmail.text?.clear()
      etRegisterPhoneNumber.text?.clear()
    }
  }
}
