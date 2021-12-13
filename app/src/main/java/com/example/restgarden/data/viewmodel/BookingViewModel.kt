package com.example.restgarden.data.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.example.restgarden.data.model.Booking
import com.example.restgarden.data.model.request.BookingTransactionRequest
import com.example.restgarden.data.model.request.ExtendAssignRequest
import com.example.restgarden.data.repository.BookingRepository
import com.example.restgarden.util.AppResource
import com.example.restgarden.util.Constants
import com.example.restgarden.util.ErrorResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class BookingViewModel @Inject constructor(private val bookingRepository: BookingRepository) :
  ViewModel() {
  
  private var _booking = MutableLiveData<AppResource<Booking>>()
  val booking: LiveData<AppResource<Booking>>
    get() = _booking
  
  private var _amount = MutableLiveData(0)
  val amount: LiveData<Int>
    get() = _amount
  
  private var _price = MutableLiveData(0.0)
  val price: LiveData<Double>
    get() = _price
  
  private var _totalPrice = MutableLiveData(0.0)
  val totalPrice: LiveData<Double>
    get() = _totalPrice
  
  fun setPrice(priceValue: Double) {
    _price.value = priceValue
  }
  
  fun increment(slot: Int) {
    if (_amount.value!! < slot) _amount.value = _amount.value?.plus(1)
    setTotalPrice()
  }
  
  fun decrement() {
    if (amount.value!! > 1) _amount.value = _amount.value?.minus(1)
    setTotalPrice()
  }
  
  fun liveDataReset() {
    _amount.value = 0
    _price.value = 0.0
    _totalPrice.value = 0.0
  }
  
  private fun setTotalPrice() {
    _totalPrice.value = _amount.value?.times(_price.value!!)
  }
  
  fun booking(description: String, graveId: String, userId: String) = liveData(Dispatchers.IO) {
    amount.value?.let {
      val transactionRequest = BookingTransactionRequest(description, graveId, userId, it)
      emit(AppResource.Loading)
      try {
        val response = bookingRepository.booking(transactionRequest)
        if (response.isSuccessful) emit(AppResource.Success(response.body()))
        else {
          val type = object : TypeToken<ErrorResponse>() {}.type
          val errorResponse: ErrorResponse =
            Gson().fromJson(response.errorBody()?.charStream(), type)
          emit(AppResource.Error(null, errorResponse.message))
        }
      } catch (e: Exception) {
        Log.i("TRANSACTION", "booking: ${e.localizedMessage}")
        emit(AppResource.Error(null, Constants.SOMETHING_WRONG))
      }
    }
  }
  
  fun getAll(userId: String) = liveData(Dispatchers.IO) {
    emit(AppResource.Loading)
    try {
      val response = bookingRepository.getAllBooking(userId)
      if (response.isSuccessful) emit(AppResource.Success(response.body()))
      else {
        val type = object : TypeToken<ErrorResponse>() {}.type
        val errorResponse: ErrorResponse = Gson().fromJson(response.errorBody()?.charStream(), type)
        emit(AppResource.Error(null, errorResponse.message))
      }
    } catch (e: Exception) {
      Log.i("TRANSACTION", "getAll: ${e.localizedMessage}")
      emit(AppResource.Error(null, Constants.SOMETHING_WRONG))
    }
  }
  
  fun getById(id: String) = viewModelScope.launch(Dispatchers.IO) {
    _booking.postValue(AppResource.Loading)
    try {
      val response = bookingRepository.getBookingById(id)
      if (response.isSuccessful) _booking.postValue(AppResource.Success(response.body()))
      else {
        val type = object : TypeToken<ErrorResponse>() {}.type
        val errorResponse: ErrorResponse = Gson().fromJson(response.errorBody()?.charStream(), type)
        _booking.postValue(AppResource.Error(null, errorResponse.message))
      }
    } catch (e: Exception) {
      Log.i("BOOKING", "getById: ${e.localizedMessage}")
      _booking.postValue(AppResource.Error(null, Constants.SOMETHING_WRONG))
    }
  }
  
  
  fun extend(id: String) = liveData(Dispatchers.IO) {
    emit(AppResource.Loading)
    try {
      val response = bookingRepository.extendBooking(ExtendAssignRequest(id))
      if (response.isSuccessful) emit(AppResource.Success(response.body()))
      else {
        val type = object : TypeToken<ErrorResponse>() {}.type
        val errorResponse: ErrorResponse = Gson().fromJson(response.errorBody()?.charStream(), type)
        emit(AppResource.Error(null, errorResponse.message))
      }
    } catch (e: Exception) {
      Log.i("BOOKING", "reSubscribe: ${e.localizedMessage}")
      emit(AppResource.Error(null, Constants.SOMETHING_WRONG))
    }
  }
  
  fun assign(id: String) = liveData(Dispatchers.IO) {
    emit(AppResource.Loading)
    try {
      val response = bookingRepository.assignBooking(ExtendAssignRequest(id))
      if (response.isSuccessful) emit(AppResource.Success(response.body()))
      else {
        val type = object : TypeToken<ErrorResponse>() {}.type
        val errorResponse: ErrorResponse = Gson().fromJson(response.errorBody()?.charStream(), type)
        emit(AppResource.Error(null, errorResponse.message))
      }
    } catch (e: Exception) {
      Log.i("BOOKING", "assign: ${e.localizedMessage}")
      emit(AppResource.Error(null, Constants.SOMETHING_WRONG))
    }
  }
  
  fun cancel(id: String) = liveData(Dispatchers.IO) {
    emit(AppResource.Loading)
    try {
      val response = bookingRepository.cancelBooking(id)
      if (response.isSuccessful) emit(AppResource.Success(response.body()))
      else emit(AppResource.Error(null, response.errorBody().toString()))
    } catch (e: Exception) {
      Log.i("Transaction", "cancelBooking: ${e.localizedMessage}")
      emit(AppResource.Error(null, Constants.SOMETHING_WRONG))
    }
  }
}
