package com.example.restgarden.data.repository

import com.example.restgarden.data.model.Transaction
import com.example.restgarden.data.model.request.BookingTransactionRequest
import retrofit2.Response

interface TransactionRepository {
  suspend fun buy(bookingTransactionRequest: BookingTransactionRequest): Response<Transaction>
  suspend fun getAllTransaction(userId: String): Response<List<Transaction>>
}
