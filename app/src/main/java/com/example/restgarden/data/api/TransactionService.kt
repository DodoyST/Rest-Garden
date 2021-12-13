package com.example.restgarden.data.api

import com.example.restgarden.data.model.Transaction
import com.example.restgarden.data.model.request.ReSubscribeAssignRequest
import com.example.restgarden.data.model.request.TransactionRequest
import com.example.restgarden.util.Constants
import retrofit2.Response
import retrofit2.http.*

interface TransactionService {
  
  @GET("${Constants.RESERVATION_URL}/${Constants.USER_URL}/{id}")
  suspend fun getAllBooking(@Path("id") userId: String): Response<List<Transaction>>
  
  @GET("${Constants.RESERVATION_URL}/{id}")
  suspend fun getBookingById(@Path("id") id: String): Response<Transaction>
  
  @POST(Constants.RESERVATION_URL)
  suspend fun booking(@Body transactionRequest: TransactionRequest): Response<Transaction>
  
  @PUT("${Constants.RESERVATION_URL}/${Constants.RESUBSCRIBE_URL}")
  suspend fun reSubscribe(@Body id: ReSubscribeAssignRequest): Response<Unit>
  
  @PUT("${Constants.RESERVATION_URL}/${Constants.ASSIGN_URL}")
  suspend fun assign(@Body id: ReSubscribeAssignRequest): Response<Unit>
  
  @DELETE("${Constants.RESERVATION_URL}/{id}")
  suspend fun cancelBooking(@Path("id") id: String): Response<Unit>
  
  @POST(Constants.TRANSACTION_URL)
  suspend fun buy(@Body transactionRequest: TransactionRequest): Response<Transaction>
  
  @GET("${Constants.TRANSACTION_URL}/${Constants.USER_URL}/{id}")
  suspend fun getAllTransaction(@Path("id") userId: String): Response<List<Transaction>>
}
