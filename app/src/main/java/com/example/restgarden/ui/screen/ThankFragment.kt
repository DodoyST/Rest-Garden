package com.example.restgarden.ui.screen

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.restgarden.R
import com.example.restgarden.databinding.FragmentThankBinding
import com.example.restgarden.ui.HomeActivity

class ThankFragment : Fragment() {
  private var _binding: FragmentThankBinding? = null
  private val binding get() = _binding!!
  
  private lateinit var homeActivity: HomeActivity
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    
    requireActivity().onBackPressedDispatcher.addCallback(
      this,
      object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
          super.setEnabled(false)
        }
      })
  }
  
  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    _binding = FragmentThankBinding.inflate(inflater, container, false)
    return binding.root
  }
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    
    homeActivity = activity as HomeActivity
    homeActivity.hideBnvHome()
    
    Handler(Looper.getMainLooper()).postDelayed({
      findNavController().navigate(R.id.action_global_homeFragment)
      homeActivity.showBnvHome()
    }, 2000)
  }
  
  override fun onDestroy() {
    super.onDestroy()
    
    _binding = null
  }
  
  
}
