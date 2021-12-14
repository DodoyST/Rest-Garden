package com.example.restgarden.data.api

import com.example.restgarden.data.model.User
import com.example.restgarden.data.model.request.UserRequest
import com.example.restgarden.data.model.request.UserUpdateRequest
import com.example.restgarden.util.Constants
import retrofit2.Response
import retrofit2.http.*

interface UserService {
  @GET("${Constants.USER_URL}/{id}")
  suspend fun getById(@Path("id") id: String): Response<User>
  
  @POST(Constants.REGISTER_URL)
  suspend fun register(@Body userRequest: UserRequest): Response<User>
  
  @PUT(Constants.USER_URL)
  suspend fun update(@Body userUpdateRequest: UserUpdateRequest): Response<User>
}
