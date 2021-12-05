package com.example.restgarden.ui.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.restgarden.databinding.FragmentProfileBinding
import com.example.restgarden.util.SessionManager
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class ProfileFragment : DaggerFragment() {
  private var _binding: FragmentProfileBinding? = null
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
    _binding = FragmentProfileBinding.inflate(inflater, container, false)
    return binding.root
  }
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    
    binding.apply {
      btnProfileLogout.setOnClickListener {
        sessionManager.clearPref()
        requireActivity().finish()
        requireActivity().startActivity(requireActivity().intent)
      }
    }
  }
  
  override fun onDestroy() {
    super.onDestroy()
    
    _binding = null
  }
}
