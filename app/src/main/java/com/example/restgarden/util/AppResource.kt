package com.example.restgarden.util

sealed class AppResource<out T>(val data: T? = null, val message: String? = null) {
  class Success<T>(data: T?) : AppResource<T>(data)
  class Error<T>(data: T? = null, message: String) : AppResource<T>(data, message)
  object Loading : AppResource<Nothing>()
}
