package com.example.restgarden.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.restgarden.R
import com.example.restgarden.data.model.SignIn
import com.example.restgarden.data.repository.AuthRepository
import com.example.restgarden.util.AppResource
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class AuthViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {
  
  fun signIn(signIn: SignIn) = liveData(Dispatchers.IO) {
    emit(AppResource.Loading)
    try {
      val response = authRepository.signIn(signIn)
      if (response.isSuccessful) emit(AppResource.Success(response.body()))
      else emit(AppResource.Error(null, response.errorBody().toString()))
    } catch (e: Exception) {
      emit(AppResource.Error(null, e.message ?: R.string.error_occurred.toString()))
    }
  }
}
