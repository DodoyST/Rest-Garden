package com.example.restgarden.data.repository

import com.example.restgarden.data.api.TransactionService
import com.example.restgarden.data.model.Booking
import com.example.restgarden.data.model.request.BookingRequest
import retrofit2.Response
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(private val transactionService: TransactionService) :
  TransactionRepository {
  override suspend fun booking(bookingRequest: BookingRequest): Response<Booking> =
    transactionService.booking(bookingRequest)
}
