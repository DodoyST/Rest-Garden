package com.example.restgarden.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.restgarden.databinding.ActivityHomeBinding
import dagger.android.AndroidInjection

class HomeActivity : AppCompatActivity() {
  
  private lateinit var binding: ActivityHomeBinding
  
  override fun onCreate(savedInstanceState: Bundle?) {
    AndroidInjection.inject(this@HomeActivity)
    super.onCreate(savedInstanceState)
    binding = ActivityHomeBinding.inflate(layoutInflater)
    setContentView(binding.root)
  }
}
