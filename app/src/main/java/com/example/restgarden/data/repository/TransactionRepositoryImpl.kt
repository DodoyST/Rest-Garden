package com.example.restgarden.data.repository

import com.example.restgarden.data.api.TransactionService
import com.example.restgarden.data.model.Transaction
import com.example.restgarden.data.model.request.ReSubscribeAssignRequest
import com.example.restgarden.data.model.request.TransactionRequest
import retrofit2.Response
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(private val transactionService: TransactionService) :
  TransactionRepository {
  
  override suspend fun getAllBooking(userId: String): Response<List<Transaction>> =
    transactionService.getAllBooking(userId)
  
  override suspend fun getBookingById(id: String): Response<Transaction> =
    transactionService.getBookingById(id)
  
  override suspend fun booking(transactionRequest: TransactionRequest): Response<Transaction> =
    transactionService.booking(transactionRequest)
  
  override suspend fun cancelBooking(id: String): Response<Unit> =
    transactionService.cancelBooking(id)
  
  override suspend fun reSubscribeBooking(id: String): Response<Unit> =
    transactionService.reSubscribe(ReSubscribeAssignRequest(id))
  
  override suspend fun assignBooking(id: String): Response<Unit> =
    transactionService.assign(ReSubscribeAssignRequest(id))
  
  override suspend fun buy(transactionRequest: TransactionRequest): Response<Transaction> =
    transactionService.buy(transactionRequest)
  
  override suspend fun getAllTransaction(userId: String): Response<List<Transaction>> =
    transactionService.getAllTransaction(userId)
}
