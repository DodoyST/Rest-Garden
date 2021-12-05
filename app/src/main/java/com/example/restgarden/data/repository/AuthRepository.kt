package com.example.restgarden.data.repository

import com.example.restgarden.data.model.SignIn
import com.example.restgarden.data.model.response.SignInResponse
import retrofit2.Response

interface AuthRepository {
  suspend fun signIn(signIn: SignIn): Response<SignInResponse>
}
