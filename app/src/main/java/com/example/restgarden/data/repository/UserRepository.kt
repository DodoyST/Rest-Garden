package com.example.restgarden.data.repository

import com.example.restgarden.data.model.User
import com.example.restgarden.data.model.request.UserRequest
import com.example.restgarden.data.model.request.UserUpdateRequest
import retrofit2.Response

interface UserRepository {
  suspend fun getUserById(id: String): Response<User>
  suspend fun registerUser(userRequest: UserRequest): Response<User>
  suspend fun updateUser(userUpdateRequest: UserUpdateRequest): Response<User>
}
