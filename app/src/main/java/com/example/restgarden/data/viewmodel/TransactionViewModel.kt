package com.example.restgarden.data.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.example.restgarden.R
import com.example.restgarden.data.model.Transaction
import com.example.restgarden.data.model.request.TransactionRequest
import com.example.restgarden.data.repository.TransactionRepository
import com.example.restgarden.util.AppResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class TransactionViewModel @Inject constructor(private val transactionRepository: TransactionRepository) :
  ViewModel() {
  
  private var _booking = MutableLiveData<AppResource<Transaction>>()
  val transaction: LiveData<AppResource<Transaction>>
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
      val transactionRequest = TransactionRequest(description, graveId, userId, it)
      emit(AppResource.Loading)
      try {
        val response = transactionRepository.booking(transactionRequest)
        if (response.isSuccessful) emit(AppResource.Success(response.body()))
        else emit(AppResource.Error(null, response.errorBody().toString()))
      } catch (e: Exception) {
        Log.i("TRANSACTION", "booking: ${e.localizedMessage}")
        emit(AppResource.Error(null, e.message ?: R.string.error_occurred.toString()))
      }
    }
  }
  
  fun getAll(userId: String) = liveData(Dispatchers.IO) {
    emit(AppResource.Loading)
    try {
      val response = transactionRepository.getAllBooking(userId)
      if (response.isSuccessful) emit(AppResource.Success(response.body()))
      else {
        Log.i("TRANSACTION", "getAll: ${response.errorBody()}")
        emit(AppResource.Error(null, response.errorBody().toString()))
      }
    } catch (e: Exception) {
      Log.i("TRANSACTION", "getAll: ${e.localizedMessage}")
      emit(AppResource.Error(null, e.message ?: R.string.error_occurred.toString()))
    }
  }
  
  fun getById(id: String) = viewModelScope.launch(Dispatchers.IO) {
    _booking.postValue(AppResource.Loading)
    try {
      val response = transactionRepository.getBookingById(id)
      if (response.isSuccessful) _booking.postValue(AppResource.Success(response.body()))
      else _booking.postValue(AppResource.Error(null, response.errorBody().toString()))
    } catch (e: Exception) {
      _booking.postValue(AppResource.Error(null, e.message ?: R.string.error_occurred.toString()))
    }
  }
  
  fun cancelBooking(id: String) = liveData(Dispatchers.IO) {
    emit(AppResource.Loading)
    try {
      val response = transactionRepository.cancelBooking(id)
      if (response.isSuccessful) emit(AppResource.Success(response.body()))
      else emit(AppResource.Error(null, response.errorBody().toString()))
    } catch (e: Exception) {
      emit(AppResource.Error(null, e.message ?: R.string.error_occurred.toString()))
    }
  }
  
  fun reSubscribe(id: String) = liveData(Dispatchers.IO) {
    emit(AppResource.Loading)
    try {
      val response = transactionRepository.reSubscribeBooking(id)
      if (response.isSuccessful) emit(AppResource.Success(response.body()))
      else emit(AppResource.Error(null, response.errorBody().toString()))
    } catch (e: Exception) {
      Log.i("BOOKING", "reSubscribe: ${e.localizedMessage}")
      emit(AppResource.Error(null, e.message ?: R.string.error_occurred.toString()))
    }
  }
  
  fun assign(id: String) = liveData(Dispatchers.IO) {
    emit(AppResource.Loading)
    try {
      val response = transactionRepository.assignBooking(id)
      if (response.isSuccessful) emit(AppResource.Success(response.body()))
      else {
        Log.i("BOOKING", "assign: ${response.errorBody()}")
        emit(AppResource.Error(null, response.errorBody().toString()))
      }
    } catch (e: Exception) {
      Log.i("BOOKING", "assign: ${e.localizedMessage}")
      emit(AppResource.Error(null, e.message ?: R.string.error_occurred.toString()))
    }
  }
  
  fun buy(description: String, graveId: String, userId: String) = liveData(Dispatchers.IO) {
    amount.value?.let {
      val transactionRequest = TransactionRequest(description, graveId, userId, it)
      emit(AppResource.Loading)
      try {
        val response = transactionRepository.buy(transactionRequest)
        if (response.isSuccessful) emit(AppResource.Success(response.body()))
        else emit(AppResource.Error(null, response.errorBody().toString()))
      } catch (e: Exception) {
        Log.i("TRANSACTION", "buy: ${e.localizedMessage}")
        emit(AppResource.Error(null, e.message ?: R.string.error_occurred.toString()))
      }
    }
  }
}
