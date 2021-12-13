package com.example.restgarden.data.repository

import com.example.restgarden.data.model.Transaction
import com.example.restgarden.data.model.request.TransactionRequest
import retrofit2.Response

interface TransactionRepository {
  suspend fun getAllBooking(userId: String): Response<List<Transaction>>
  suspend fun getBookingById(id: String): Response<Transaction>
  suspend fun booking(transactionRequest: TransactionRequest): Response<Transaction>
  suspend fun cancelBooking(id: String): Response<Unit>
  suspend fun reSubscribeBooking(id: String): Response<Unit>
  suspend fun assignBooking(id: String): Response<Unit>
  suspend fun buy(transactionRequest: TransactionRequest): Response<Transaction>
  suspend fun getAllTransaction(userId: String): Response<List<Transaction>>
}
