package com.example.restgarden.util

import android.content.SharedPreferences

class SessionManager(private val sharedPreferences: SharedPreferences) {
  
  companion object {
    const val TOKEN = "token"
    const val USERNAME = "username"
    const val IS_LOGGED_IN = "is_logged_in"
  }
  
  fun saveAuthToken(token: String, username: String) {
    val editor = sharedPreferences.edit()
    editor.putString(TOKEN, token)
    editor.putString(USERNAME, username)
    editor.apply()
  }
  
  fun fetchAuthToken(): String? {
    return sharedPreferences.getString(TOKEN, null)
  }
  
  fun fetchAuthUsername(): String? {
    return sharedPreferences.getString(USERNAME, null)
  }
  
  fun isLoggedIn(): Boolean = sharedPreferences.getBoolean(IS_LOGGED_IN, false)
  
  fun setLoggedIn(loggedIn: Boolean) {
    sharedPreferences.edit().putBoolean(IS_LOGGED_IN, loggedIn).apply()
  }
  
  fun clearPref() {
    sharedPreferences.edit().clear().apply()
  }
}
