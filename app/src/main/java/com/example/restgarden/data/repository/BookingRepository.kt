package com.example.restgarden.data.repository

import com.example.restgarden.data.model.Booking
import com.example.restgarden.data.model.request.BookingTransactionRequest
import com.example.restgarden.data.model.request.ExtendAssignRequest
import retrofit2.Response

interface BookingRepository {
  suspend fun booking(bookingTransactionRequest: BookingTransactionRequest): Response<Booking>
  suspend fun getAllBooking(userId: String): Response<List<Booking>>
  suspend fun getBookingById(id: String): Response<Booking>
  suspend fun extendBooking(extendAssignRequest: ExtendAssignRequest): Response<Unit>
  suspend fun assignBooking(extendAssignRequest: ExtendAssignRequest): Response<Unit>
  suspend fun cancelBooking(id: String): Response<Unit>
}
