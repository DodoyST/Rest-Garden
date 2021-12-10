package com.example.restgarden.data.repository

import com.example.restgarden.data.api.TransactionService
import com.example.restgarden.data.model.Booking
import com.example.restgarden.data.model.request.BookingRequest
import retrofit2.Response
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(private val transactionService: TransactionService) :
  TransactionRepository {
  
  override suspend fun getAllBooking(): Response<List<Booking>> = transactionService.getAllBooking()
  override suspend fun getBokkingById(id: String): Response<Booking> =
    transactionService.getBookingById(id)
  
  override suspend fun booking(bookingRequest: BookingRequest): Response<Booking> =
    transactionService.booking(bookingRequest)
}
