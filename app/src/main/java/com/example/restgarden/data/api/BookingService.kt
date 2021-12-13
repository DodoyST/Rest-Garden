package com.example.restgarden.data.api

import com.example.restgarden.data.model.Booking
import com.example.restgarden.data.model.request.BookingTransactionRequest
import com.example.restgarden.data.model.request.ExtendAssignRequest
import com.example.restgarden.util.Constants
import retrofit2.Response
import retrofit2.http.*

interface BookingService {
  
  @POST(Constants.RESERVATION_URL)
  suspend fun booking(@Body bookingTransactionRequest: BookingTransactionRequest): Response<Booking>
  
  @GET("${Constants.RESERVATION_URL}/${Constants.USER_URL}/{id}")
  suspend fun getAll(@Path("id") userId: String): Response<List<Booking>>
  
  @GET("${Constants.RESERVATION_URL}/{id}")
  suspend fun getById(@Path("id") id: String): Response<Booking>
  
  @PUT("${Constants.RESERVATION_URL}/${Constants.RESUBSCRIBE_URL}")
  suspend fun extend(@Body extendAssignRequest: ExtendAssignRequest): Response<Unit>
  
  @PUT("${Constants.RESERVATION_URL}/${Constants.ASSIGN_URL}")
  suspend fun assign(@Body extendAssignRequest: ExtendAssignRequest): Response<Unit>
  
  @DELETE("${Constants.RESERVATION_URL}/{id}")
  suspend fun cancel(@Path("id") id: String): Response<Unit>
  
}
