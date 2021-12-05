package com.example.restgarden.data.api

import com.example.restgarden.data.model.SignIn
import com.example.restgarden.data.model.response.SignInResponse
import com.example.restgarden.util.Constants
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
  
  @POST(Constants.LOGIN_URL)
  suspend fun signIn(@Body signIn: SignIn): Response<SignInResponse>
}
