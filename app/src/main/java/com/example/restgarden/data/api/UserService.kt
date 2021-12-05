package com.example.restgarden.data.api

import com.example.restgarden.data.model.User
import com.example.restgarden.data.model.request.UserRequest
import com.example.restgarden.util.Constants
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface UserService {
  @GET("${Constants.USER_URL}/{id}")
  suspend fun getById(@Path("id") id: String): Response<User>
  
  @POST(Constants.REGISTER_URL)
  suspend fun register(@Body userRequest: UserRequest): Response<User>
}
