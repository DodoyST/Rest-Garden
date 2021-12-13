package com.example.restgarden.data.repository

import com.example.restgarden.data.api.BookingService
import com.example.restgarden.data.model.Booking
import com.example.restgarden.data.model.request.BookingTransactionRequest
import com.example.restgarden.data.model.request.ExtendAssignRequest
import retrofit2.Response
import javax.inject.Inject

class BookingRepositoryImpl @Inject constructor(private val bookingService: BookingService) :
  BookingRepository {
  override suspend fun booking(bookingTransactionRequest: BookingTransactionRequest): Response<Booking> =
    bookingService.booking(bookingTransactionRequest)
  
  override suspend fun getAllBooking(userId: String): Response<List<Booking>> =
    bookingService.getAll(userId)
  
  override suspend fun getBookingById(id: String): Response<Booking> = bookingService.getById(id)
  override suspend fun extendBooking(extendAssignRequest: ExtendAssignRequest): Response<Unit> =
    bookingService.extend(extendAssignRequest)
  
  override suspend fun assignBooking(extendAssignRequest: ExtendAssignRequest): Response<Unit> =
    bookingService.assign(extendAssignRequest)
  
  override suspend fun cancelBooking(id: String): Response<Unit> = bookingService.cancel(id)
}
