package com.example.restgarden.data.api

import com.example.restgarden.data.model.Booking
import com.example.restgarden.data.model.request.BookingRequest
import com.example.restgarden.util.Constants
import retrofit2.Response
import retrofit2.http.*

interface TransactionService {
  
  @GET("${Constants.RESERVATION_URL}/${Constants.USER_URL}/{id}")
  suspend fun getAllBooking(@Path("id") userId: String): Response<List<Booking>>
  
  @GET("${Constants.RESERVATION_URL}/{id}")
  suspend fun getBookingById(@Path("id") id: String): Response<Booking>
  
  @POST(Constants.RESERVATION_URL)
  suspend fun booking(@Body bookingRequest: BookingRequest): Response<Booking>
  
  @DELETE("${Constants.RESERVATION_URL}/{id}")
  suspend fun cancelBooking(@Path("id") id: String): Response<Unit>
}
