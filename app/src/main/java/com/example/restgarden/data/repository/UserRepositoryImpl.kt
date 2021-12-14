package com.example.restgarden.data.repository

import com.example.restgarden.data.api.UserService
import com.example.restgarden.data.model.User
import com.example.restgarden.data.model.request.UserRequest
import com.example.restgarden.data.model.request.UserUpdateRequest
import retrofit2.Response
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(private val userService: UserService) :
  UserRepository {
  override suspend fun getUserById(id: String): Response<User> = userService.getById(id)
  override suspend fun registerUser(userRequest: UserRequest): Response<User> =
    userService.register(userRequest)
  
  override suspend fun updateUser(userUpdateRequest: UserUpdateRequest): Response<User> =
    userService.update(userUpdateRequest)
}
