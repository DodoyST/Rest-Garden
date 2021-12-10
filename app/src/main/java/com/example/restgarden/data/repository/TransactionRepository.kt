package com.example.restgarden.data.repository

import com.example.restgarden.data.model.Booking
import com.example.restgarden.data.model.request.BookingRequest
import retrofit2.Response

interface TransactionRepository {
  suspend fun getAllBooking(userId: String): Response<List<Booking>>
  suspend fun getBookingById(id: String): Response<Booking>
  suspend fun booking(bookingRequest: BookingRequest): Response<Booking>
  suspend fun cancelBooking(id: String): Response<Unit>
}
