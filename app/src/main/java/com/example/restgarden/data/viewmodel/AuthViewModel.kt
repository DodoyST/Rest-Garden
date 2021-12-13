package com.example.restgarden.data.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.restgarden.data.model.SignIn
import com.example.restgarden.data.repository.AuthRepository
import com.example.restgarden.util.AppResource
import com.example.restgarden.util.Constants
import com.example.restgarden.util.ErrorResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class AuthViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {
  
  fun signIn(signIn: SignIn) = liveData(Dispatchers.IO) {
    emit(AppResource.Loading)
    try {
      val response = authRepository.signIn(signIn)
      if (response.isSuccessful) emit(AppResource.Success(response.body()))
      else {
        val type = object : TypeToken<ErrorResponse>() {}.type
        val errorResponse: ErrorResponse = Gson().fromJson(response.errorBody()?.charStream(), type)
        emit(AppResource.Error(null, errorResponse.message))
        Log.i("AUTH", "signIn: ${response.errorBody()}")
      }
    } catch (e: Exception) {
      emit(AppResource.Error(null, Constants.SOMETHING_WRONG))
      Log.i("AUTH", "signIn: ${e.localizedMessage}")
    }
  }
}
