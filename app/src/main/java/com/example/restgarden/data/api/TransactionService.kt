package com.example.restgarden.data.api

import com.example.restgarden.data.model.Transaction
import com.example.restgarden.data.model.request.BookingTransactionRequest
import com.example.restgarden.util.Constants
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface TransactionService {
  
  @POST(Constants.TRANSACTION_URL)
  suspend fun buy(@Body bookingTransactionRequest: BookingTransactionRequest): Response<Transaction>
  
  @GET("${Constants.TRANSACTION_URL}/${Constants.USER_URL}/{id}")
  suspend fun getAll(@Path("id") userId: String): Response<List<Transaction>>
}
