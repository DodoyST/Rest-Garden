package com.example.restgarden.di.module

import com.example.restgarden.data.api.ApiService
import com.example.restgarden.data.api.AuthInterceptors
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
  fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)
  
  @Provides
  @Singleton
  fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
    Retrofit.Builder().baseUrl(Constants.BASE_URL)
      .addConverterFactory(GsonConverterFactory.create()).build()
  
  @Provides
  @Singleton
  fun provideOkHttpClient(authInterceptors: AuthInterceptors): OkHttpClient =
    OkHttpClient.Builder().addInterceptor(authInterceptors).build()
}
