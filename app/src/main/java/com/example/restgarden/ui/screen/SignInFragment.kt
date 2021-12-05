package com.example.restgarden.ui.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.restgarden.R
import com.example.restgarden.databinding.FragmentSignInBinding
import com.example.restgarden.util.SessionManager
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class SignInFragment : DaggerFragment() {
  private var _binding: FragmentSignInBinding? = null
  private val binding get() = _binding!!
  
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
    
    binding.apply {
      btnSignInSubmit.setOnClickListener {
        sessionManager.setLoggedIn(true)
      }
      
      btnSignInRegister.setOnClickListener {
        findNavController().navigate(R.id.action_global_signInFragment_to_registerFragment)
      }
    }
  }
  
  override fun onDestroy() {
    super.onDestroy()
    
    _binding = null
  }
}
