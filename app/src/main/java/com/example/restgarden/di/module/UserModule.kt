package com.example.restgarden.di.module

import com.example.restgarden.data.repository.UserRepository
import com.example.restgarden.data.repository.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class UserModule {
  
  @Binds
  @Singleton
  abstract fun userRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository
}
