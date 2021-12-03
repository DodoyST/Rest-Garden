package com.example.restgarden.data.api

import com.example.restgarden.data.model.Grave
import com.example.restgarden.util.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path


interface ApiService {
  @GET("${Constants.GRAVE_URL}s")
  suspend fun getAllGrave(): Response<List<Grave>>
  
  @GET("${Constants.GRAVE_URL}/{id}")
  suspend fun getGraveById(@Path("id") id: String): Response<Grave>
}
