package com.example.restgarden.data.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.restgarden.R
import com.example.restgarden.data.model.request.UserRequest
import com.example.restgarden.data.repository.UserRepository
import com.example.restgarden.util.AppResource
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class UserViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {
  fun getById(id: String) = liveData(Dispatchers.IO) {
    emit(AppResource.Loading)
    try {
      val response = userRepository.getUserById(id)
      if (response.isSuccessful) emit(AppResource.Success(response.body()))
      else {
        emit(AppResource.Error(null, response.errorBody().toString()))
        Log.i("USER", "getById: ${response.errorBody()}")
      }
    } catch (e: Exception) {
      emit(AppResource.Error(null, e.message ?: R.string.error_occurred.toString()))
      Log.i("USER", "getById: ${e.localizedMessage}")
    }
  }
  
  fun register(userRequest: UserRequest) = liveData(Dispatchers.IO) {
    emit(AppResource.Loading)
    try {
      val response = userRepository.registerUser(userRequest)
      if (response.isSuccessful) emit(AppResource.Success(response.body()))
      else {
        emit(AppResource.Error(null, response.errorBody().toString()))
        Log.i("USER", "register: ${response.errorBody()}")
      }
    } catch (e: Exception) {
      emit(AppResource.Error(null, e.message ?: R.string.error_occurred.toString()))
      Log.i("USER", "register: ${e.localizedMessage}")
    }
  }
}
