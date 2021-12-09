package com.example.restgarden.di.module

import com.example.restgarden.data.api.*
import com.example.restgarden.util.Constants
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class ApiModule {
  
  @Provides
  @Singleton
  fun provideGraveService(retrofit: Retrofit): GraveService =
    retrofit.create(GraveService::class.java)
  
  @Provides
  @Singleton
  fun provideAuthService(retrofit: Retrofit): AuthService = retrofit.create(AuthService::class.java)
  
  @Provides
  @Singleton
  fun provideUserService(retrofit: Retrofit): UserService = retrofit.create(UserService::class.java)
  
  @Provides
  @Singleton
  fun provideTransactionService(retrofit: Retrofit): TransactionService =
    retrofit.create(TransactionService::class.java)
  
  @Provides
  @Singleton
  fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
    Retrofit.Builder().baseUrl(Constants.BASE_URL)
      .addConverterFactory(GsonConverterFactory.create()).client(okHttpClient).build()
  
  @Provides
  @Singleton
  fun provideOkHttpClient(authInterceptors: AuthInterceptors): OkHttpClient =
    OkHttpClient.Builder().addInterceptor(authInterceptors).build()
}
