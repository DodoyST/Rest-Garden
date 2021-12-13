package com.example.restgarden.di.module

import com.example.restgarden.data.repository.BookingRepository
import com.example.restgarden.data.repository.BookingRepositoryImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class BookingModule {
  
  @Binds
  @Singleton
  abstract fun bindBookingRepository(bookingRepositoryImpl: BookingRepositoryImpl): BookingRepository
}
