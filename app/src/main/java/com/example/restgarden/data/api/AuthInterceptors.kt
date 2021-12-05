package com.example.restgarden.data.api

import com.example.restgarden.util.Constants
import com.example.restgarden.util.SessionManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptors @Inject constructor(private val sessionManager: SessionManager) :
  Interceptor {
  override fun intercept(chain: Interceptor.Chain): Response {
    val originalRequest = chain.request()
    val url = originalRequest.url.toString()
    if (!url.contains(Constants.SIGN_IN_URL) && !url.contains(Constants.GRAVE_URL)) {
      val requestBuilder = originalRequest.newBuilder()
      requestBuilder.addHeader(
        "Authorization",
        "Bearer ${sessionManager.fetchAuthToken()}"
      )
      return chain.proceed(requestBuilder.build())
    }
    return chain.proceed(originalRequest)
  }
}
