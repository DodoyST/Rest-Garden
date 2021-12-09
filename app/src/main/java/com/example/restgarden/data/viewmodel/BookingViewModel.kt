package com.example.restgarden.data.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.restgarden.R
import com.example.restgarden.data.model.request.BookingRequest
import com.example.restgarden.data.repository.TransactionRepository
import com.example.restgarden.util.AppResource
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class BookingViewModel @Inject constructor(private val transactionRepository: TransactionRepository) :
  ViewModel() {
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
  
  fun increment() {
    _amount.value = _amount.value?.plus(1)
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
      val bookingRequest = BookingRequest(description, graveId, userId, it)
      emit(AppResource.Loading)
      try {
        val response = transactionRepository.booking(bookingRequest)
        if (response.isSuccessful) emit(AppResource.Success(response.body()))
        else emit(AppResource.Error(null, response.errorBody().toString()))
      } catch (e: Exception) {
        Log.i("BOOKING", "booking: ${e.localizedMessage}")
        emit(AppResource.Error(null, e.message ?: R.string.error_occurred.toString()))
      }
    }
  }
  
  fun getAll() = liveData(Dispatchers.IO) {
    emit(AppResource.Loading)
    try {
      val response = transactionRepository.getAllBooking()
      if (response.isSuccessful) emit(AppResource.Success(response.body()))
      else emit(AppResource.Error(null, response.errorBody().toString()))
    } catch (e: Exception) {
      emit(AppResource.Error(null, e.message ?: R.string.error_occurred.toString()))
    }
  }
}
