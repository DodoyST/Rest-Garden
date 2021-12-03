package com.example.restgarden.data.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.restgarden.R
import com.example.restgarden.data.repository.GraveRepository
import com.example.restgarden.util.AppResource
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class GraveViewModel @Inject constructor(private val graveRepository: GraveRepository) :
  ViewModel() {
  fun getAll() = liveData(Dispatchers.IO) {
    emit(AppResource.Loading)
    try {
      val response = graveRepository.getAllGraves()
      if (response.isSuccessful) emit(AppResource.Success(response.body()))
      else {
        emit(AppResource.Error(null, response.errorBody().toString()))
        Log.i("GRAVE", "getAllGrave: ${response.errorBody()}")
      }
    } catch (e: Exception) {
      emit(AppResource.Error(null, e.message ?: R.string.error_occurred.toString()))
      Log.i("GRAVE", "getAllGrave: ${e.localizedMessage}")
    }
  }
}
