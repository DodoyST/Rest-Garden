package com.example.restgarden.data.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.restgarden.data.model.Booking
import com.example.restgarden.data.model.request.BookingTransactionRequest
import com.example.restgarden.data.repository.TransactionRepository
import com.example.restgarden.util.AppResource
import com.example.restgarden.util.Constants
import com.example.restgarden.util.ErrorResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class TransactionViewModel @Inject constructor(private val transactionRepository: TransactionRepository) :
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
  
  fun buy(description: String, graveId: String, userId: String) = liveData(Dispatchers.IO) {
    amount.value?.let {
      val transactionRequest = BookingTransactionRequest(description, graveId, userId, it)
      emit(AppResource.Loading)
      try {
        val response = transactionRepository.buy(transactionRequest)
        if (response.isSuccessful) emit(AppResource.Success(response.body()))
        else {
          val type = object : TypeToken<ErrorResponse>() {}.type
          val errorResponse: ErrorResponse =
            Gson().fromJson(response.errorBody()?.charStream(), type)
          emit(AppResource.Error(null, errorResponse.message))
        }
      } catch (e: Exception) {
        Log.i("TRANSACTION", "buy: ${e.localizedMessage}")
        emit(AppResource.Error(null, Constants.SOMETHING_WRONG))
      }
    }
  }
  
  fun getAllTransaction(userId: String) = liveData(Dispatchers.IO) {
    emit(AppResource.Loading)
    try {
      val response = transactionRepository.getAllTransaction(userId)
      if (response.isSuccessful) emit(AppResource.Success(response.body()))
      else {
        val type = object : TypeToken<ErrorResponse>() {}.type
        val errorResponse: ErrorResponse = Gson().fromJson(response.errorBody()?.charStream(), type)
        emit(AppResource.Error(null, errorResponse.message))
      }
    } catch (e: Exception) {
      Log.i("TRANSACTION", "getAllTransaction: ${e.localizedMessage}")
      emit(AppResource.Error(null, Constants.SOMETHING_WRONG))
    }
  }
}
