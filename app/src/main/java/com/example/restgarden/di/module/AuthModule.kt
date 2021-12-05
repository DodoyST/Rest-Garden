package com.example.restgarden.di.module

import com.example.restgarden.data.repository.AuthRepository
import com.example.restgarden.data.repository.AuthRepositoryImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class AuthModule {
  
  @Binds
  @Singleton
  abstract fun bindAuthRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository
}
