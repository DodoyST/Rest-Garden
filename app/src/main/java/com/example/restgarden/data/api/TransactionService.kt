package com.example.restgarden.data.api

import com.example.restgarden.data.model.Booking
import com.example.restgarden.data.model.request.BookingRequest
import com.example.restgarden.util.Constants
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface TransactionService {
  
  @GET("${Constants.RESERVATION_URL}s")
  suspend fun getAllBooking(): Response<List<Booking>>
  
  @GET("${Constants.RESERVATION_URL}/{id}")
  suspend fun getBookingById(@Path("id") id: String): Response<Booking>
  
  @POST(Constants.RESERVATION_URL)
  suspend fun booking(@Body bookingRequest: BookingRequest): Response<Booking>
}