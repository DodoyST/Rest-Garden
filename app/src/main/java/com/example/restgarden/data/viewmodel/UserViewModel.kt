package com.example.restgarden.data.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.restgarden.data.model.request.UserRequest
import com.example.restgarden.data.repository.UserRepository
import com.example.restgarden.util.AppResource
import com.example.restgarden.util.Constants
import com.example.restgarden.util.ErrorResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class UserViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {
  fun getById(id: String) = liveData(Dispatchers.IO) {
    emit(AppResource.Loading)
    try {
      val response = userRepository.getUserById(id)
      if (response.isSuccessful) emit(AppResource.Success(response.body()))
      else {
        val type = object : TypeToken<ErrorResponse>() {}.type
        val errorResponse: ErrorResponse = Gson().fromJson(response.errorBody()?.charStream(), type)
        emit(AppResource.Error(null, errorResponse.message))
        Log.i("USER", "getById: ${response.errorBody()}")
      }
    } catch (e: Exception) {
      emit(AppResource.Error(null, Constants.SOMETHING_WRONG))
      Log.i("USER", "getById: ${e.localizedMessage}")
    }
  }
  
  fun register(userRequest: UserRequest) = liveData(Dispatchers.IO) {
    emit(AppResource.Loading)
    try {
      val response = userRepository.registerUser(userRequest)
      if (response.isSuccessful) emit(AppResource.Success(response.body()))
      else {
        val type = object : TypeToken<ErrorResponse>() {}.type
        val errorResponse: ErrorResponse = Gson().fromJson(response.errorBody()?.charStream(), type)
        emit(AppResource.Error(null, errorResponse.message))
        Log.i("USER", "register: ${response.errorBody()!!.string()}")
      }
    } catch (e: Exception) {
      emit(AppResource.Error(null, Constants.SOMETHING_WRONG))
      Log.i("USER", "register: ${e.localizedMessage}")
    }
  }
}
