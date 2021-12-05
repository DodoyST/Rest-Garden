package com.example.restgarden.ui

import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.restgarden.databinding.ActivityHomeBinding
import com.example.restgarden.util.SessionManager
import dagger.android.AndroidInjection
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class HomeActivity : DaggerAppCompatActivity() {
  
  private lateinit var binding: ActivityHomeBinding
  private lateinit var navHostFragment: NavHostFragment
  private lateinit var navController: NavController
  
  @Inject
  lateinit var sessionManager: SessionManager
  
  override fun onCreate(savedInstanceState: Bundle?) {
    AndroidInjection.inject(this@HomeActivity)
    super.onCreate(savedInstanceState)
    binding = ActivityHomeBinding.inflate(layoutInflater)
    setContentView(binding.root)
    
    navHostFragment =
      supportFragmentManager.findFragmentById(binding.navHostHome.id) as NavHostFragment
    navController = navHostFragment.findNavController()
    binding.bnvHome.setupWithNavController(navController)
    
    if (sessionManager.fetchAuthToken().isNullOrEmpty()) {
      sessionManager.clearPref()
    }
  }
  
  override fun onStart() {
    super.onStart()
    
    if (sessionManager.isLoggedIn()) showBnvHome()
    else hideBnvHome()
  }
  
  fun hideBnvHome() {
    binding.bnvHome.visibility = View.GONE
  }
  
  fun showBnvHome() {
    binding.bnvHome.visibility = View.VISIBLE
  }
}
