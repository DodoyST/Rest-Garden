package com.example.restgarden.data.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.example.restgarden.R
import com.example.restgarden.data.model.Grave
import com.example.restgarden.data.repository.GraveRepository
import com.example.restgarden.util.AppResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class GraveViewModel @Inject constructor(private val graveRepository: GraveRepository) :
  ViewModel() {
  
  private var _grave = MutableLiveData<AppResource<Grave>>()
  val grave: LiveData<AppResource<Grave>>
    get() = _grave
  
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
  
  fun getById(id: String) = viewModelScope.launch(Dispatchers.IO) {
    _grave.postValue(AppResource.Loading)
    try {
      val response = graveRepository.getGraveById(id)
      if (response.isSuccessful) _grave.postValue(AppResource.Success(response.body()))
      else {
        _grave.postValue(AppResource.Error(null, response.errorBody().toString()))
        Log.i("GRAVE", "getGraveById: ${response.errorBody()}")
      }
    } catch (e: Exception) {
      _grave.postValue(AppResource.Error(null, e.message ?: R.string.error_occurred.toString()))
      Log.i("GRAVE", "getGraveById: ${e.localizedMessage}")
    }
  }
  
  fun clearGrave() {
    _grave.value = AppResource.Success(Grave("", "", 0, 0.0, "", "", "", "", ""))
  }
}
