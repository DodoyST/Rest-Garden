package com.example.restgarden.data.api

import com.example.restgarden.data.model.Grave
import com.example.restgarden.util.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path


interface GraveService {
  
  @GET("${Constants.GRAVE_URL}s")
  suspend fun getAll(): Response<List<Grave>>
  
  @GET("${Constants.GRAVE_URL}/{id}")
  suspend fun getById(@Path("id") id: String): Response<Grave>
}
