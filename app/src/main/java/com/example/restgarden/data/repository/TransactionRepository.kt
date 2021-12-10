package com.example.restgarden.data.repository

import com.example.restgarden.data.model.Booking
import com.example.restgarden.data.model.request.BookingRequest
import retrofit2.Response

interface TransactionRepository {
  suspend fun getAllBooking(): Response<List<Booking>>
  suspend fun getBokkingById(id: String): Response<Booking>
  suspend fun booking(bookingRequest: BookingRequest): Response<Booking>
}