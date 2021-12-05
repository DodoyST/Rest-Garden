package com.example.restgarden.data.repository

import com.example.restgarden.data.api.AuthService
import com.example.restgarden.data.model.SignIn
import com.example.restgarden.data.model.response.SignInResponse
import retrofit2.Response
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(private val authService: AuthService) :
  AuthRepository {
  override suspend fun signIn(signIn: SignIn): Response<SignInResponse> = authService.signIn(signIn)
}
