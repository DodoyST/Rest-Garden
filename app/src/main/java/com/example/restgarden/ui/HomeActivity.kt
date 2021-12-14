package com.example.restgarden.ui

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.restgarden.data.repository.UserRepository
import com.example.restgarden.data.viewmodel.UserViewModel
import com.example.restgarden.databinding.ActivityHomeBinding
import com.example.restgarden.util.AppResource
import com.example.restgarden.util.SessionManager
import com.google.android.material.snackbar.Snackbar
import dagger.android.AndroidInjection
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class HomeActivity : DaggerAppCompatActivity() {
  
  private lateinit var binding: ActivityHomeBinding
  private lateinit var navHostFragment: NavHostFragment
  private lateinit var navController: NavController
  
  private lateinit var userViewModel: UserViewModel
  
  @Inject
  lateinit var userRepository: UserRepository
  
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
    
    instanceViewModel()
    
    if (sessionManager.isLoggedIn()) showBnvHome()
    else hideBnvHome()
    
    subscribe()
  }
  
  fun hideBnvHome() {
    binding.bnvHome.visibility = View.GONE
  }
  
  fun showBnvHome() {
    binding.bnvHome.visibility = View.VISIBLE
  }
  
  private fun instanceViewModel() {
    userViewModel = ViewModelProvider(viewModelStore, object : ViewModelProvider.Factory {
      override fun <T : ViewModel> create(modelClass: Class<T>): T =
        UserViewModel(userRepository) as T
    })[UserViewModel::class.java]
  }
  
  private fun subscribe() {
    sessionManager.fetchAuthId()?.let { userViewModel.getById(it) }
    
    userViewModel.user.observe(this, {
      when (it) {
        is AppResource.Error -> it.message?.let { it1 -> subscribeError(it1) }
        else -> {}
      }
    })
  }
  
  private fun subscribeError(message: String) {
    Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    signOut()
  }
  
  private fun signOut() {
    sessionManager.clearPref()
    finish()
    startActivity(intent)
  }
}
