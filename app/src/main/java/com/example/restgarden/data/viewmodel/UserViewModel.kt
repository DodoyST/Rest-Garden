package com.example.restgarden.data.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.example.restgarden.data.model.User
import com.example.restgarden.data.model.request.UserRequest
import com.example.restgarden.data.model.request.UserUpdateRequest
import com.example.restgarden.data.repository.UserRepository
import com.example.restgarden.util.AppResource
import com.example.restgarden.util.Constants
import com.example.restgarden.util.ErrorResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class UserViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {
  
  private var _user = MutableLiveData<AppResource<User>>()
  val user: LiveData<AppResource<User>>
    get() = _user
  
  fun getById(id: String) = viewModelScope.launch(Dispatchers.IO) {
    _user.postValue(AppResource.Loading)
    try {
      val response = userRepository.getUserById(id)
      if (response.isSuccessful) _user.postValue(AppResource.Success(response.body()))
      else {
        val type = object : TypeToken<ErrorResponse>() {}.type
        val errorResponse: ErrorResponse = Gson().fromJson(response.errorBody()?.charStream(), type)
        _user.postValue(AppResource.Error(null, errorResponse.message))
      }
    } catch (e: Exception) {
      _user.postValue(AppResource.Error(null, Constants.SOMETHING_WRONG))
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
      }
    } catch (e: Exception) {
      emit(AppResource.Error(null, Constants.SOMETHING_WRONG))
      Log.i("USER", "register: ${e.localizedMessage}")
    }
  }
  
  fun update(userUpdateRequest: UserUpdateRequest) = liveData(Dispatchers.IO) {
    emit(AppResource.Loading)
    try {
      val response = userRepository.updateUser(userUpdateRequest)
      if (response.isSuccessful) emit(AppResource.Success(response.body()))
      else {
        val type = object : TypeToken<ErrorResponse>() {}.type
        val errorResponse: ErrorResponse = Gson().fromJson(response.errorBody()?.charStream(), type)
        emit(AppResource.Error(null, errorResponse.message))
      }
    } catch (e: Exception) {
      emit(AppResource.Error(null, Constants.SOMETHING_WRONG))
      Log.i("USER", "register: ${e.localizedMessage}")
    }
  }
  
  fun setUser(user: User) {
    _user.value = AppResource.Success(user)
  }
}
