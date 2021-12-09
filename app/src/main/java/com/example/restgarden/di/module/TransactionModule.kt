package com.example.restgarden.di.module

import com.example.restgarden.data.repository.TransactionRepository
import com.example.restgarden.data.repository.TransactionRepositoryImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class TransactionModule {
  
  @Binds
  @Singleton
  abstract fun bindTransactionRepository(transactionRepositoryImpl: TransactionRepositoryImpl): TransactionRepository
}
