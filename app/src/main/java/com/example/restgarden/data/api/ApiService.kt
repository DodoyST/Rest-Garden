package com.example.restgarden.data.api

import com.example.restgarden.data.model.Grave
import com.example.restgarden.util.Constants
import retrofit2.Response
import retrofit2.http.GET


interface ApiService {
  @GET(Constants.GRAVE_URL)
  suspend fun getAllGrave(): Response<List<Grave>>
}
