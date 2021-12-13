package com.example.restgarden.data.repository

import com.example.restgarden.data.api.TransactionService
import com.example.restgarden.data.model.Transaction
import com.example.restgarden.data.model.request.BookingTransactionRequest
import retrofit2.Response
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(private val transactionService: TransactionService) :
  TransactionRepository {
  override suspend fun buy(bookingTransactionRequest: BookingTransactionRequest): Response<Transaction> =
    transactionService.buy(bookingTransactionRequest)
  
  override suspend fun getAllTransaction(userId: String): Response<List<Transaction>> =
    transactionService.getAll(userId)
}
