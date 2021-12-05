package com.example.restgarden.ui.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.restgarden.R
import com.example.restgarden.data.model.SignIn
import com.example.restgarden.data.model.response.SignInResponse
import com.example.restgarden.data.repository.AuthRepositoryImpl
import com.example.restgarden.data.viewmodel.AuthViewModel
import com.example.restgarden.databinding.FragmentSignInBinding
import com.example.restgarden.util.AppResource
import com.example.restgarden.util.SessionManager
import com.example.restgarden.util.hideKeyboard
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class SignInFragment : DaggerFragment() {
  private var _binding: FragmentSignInBinding? = null
  private val binding get() = _binding!!
  
  private lateinit var authViewModel: AuthViewModel
  
  @Inject
  lateinit var authRepositoryImpl: AuthRepositoryImpl
  
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
    _binding = FragmentSignInBinding.inflate(inflater, container, false)
    return binding.root
  }
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    
    authViewModel = ViewModelProvider(requireActivity(), object : ViewModelProvider.Factory {
      override fun <T : ViewModel> create(modelClass: Class<T>): T =
        AuthViewModel(authRepositoryImpl) as T
    })[AuthViewModel::class.java]
    
    binding.apply {
      btnSignInSubmit.setOnClickListener {
        signIn()
      }
      
      btnSignInRegister.setOnClickListener {
        findNavController().navigate(R.id.action_global_registerFragment)
      }
    }
  }
  
  override fun onDestroy() {
    super.onDestroy()
    
    _binding = null
  }
  
  private fun signIn() {
    binding.apply {
      authViewModel.signIn(
        SignIn(
          etSignInUsername.text.toString(),
          etSignInPassword.text.toString()
        )
      ).observe(viewLifecycleOwner, {
        when (it) {
          is AppResource.Success -> if (it.data != null) signInSuccess(it.data)
          is AppResource.Error -> signInError()
          is AppResource.Loading -> isLoading()
        }
      })
      requireActivity().hideKeyboard(requireView())
    }
  }
  
  private fun signInSuccess(response: SignInResponse) {
    sessionManager.setLoggedIn(true)
    sessionManager.saveAuthToken(response.token, response.id)
    isNotLoading()
    formClear()
    requireActivity().finish()
    findNavController().navigate(R.id.action_global_homeActivity)
  }
  
  private fun signInError() {
    Snackbar.make(requireView(), getString(R.string.sign_in_error), Snackbar.LENGTH_LONG).show()
    isNotLoading()
  }
  
  private fun isNotLoading() {
    binding.apply {
      etSignInUsername.isEnabled = true
      etSignInPassword.isEnabled = true
      btnSignInSubmit.isEnabled = true
      btnSignInSubmit.visibility = View.VISIBLE
      btnSignInRegister.isEnabled = true
      btnSignInRegister.visibility = View.VISIBLE
    }
  }
  
  private fun isLoading() {
    binding.apply {
      etSignInUsername.isEnabled = false
      etSignInPassword.isEnabled = false
      btnSignInSubmit.isEnabled = false
      btnSignInSubmit.visibility = View.INVISIBLE
      btnSignInRegister.isEnabled = false
      btnSignInRegister.visibility = View.INVISIBLE
    }
  }
  
  private fun formClear() {
    binding.apply {
      etSignInUsername.text?.clear()
      etSignInPassword.text?.clear()
    }
  }
}
