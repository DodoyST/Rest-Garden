package com.example.restgarden.di.module

import com.example.restgarden.data.repository.GraveRepository
import com.example.restgarden.data.repository.GraveRepositoryImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class GraveModule {
  
  @Binds
  @Singleton
  abstract fun bindGraveRepository(graveRepositoryImpl: GraveRepositoryImpl): GraveRepository
}
