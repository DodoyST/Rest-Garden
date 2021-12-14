package com.example.restgarden.data.model.request

data class UserUpdateRequest(
  var id: String,
  var name: String,
  var username: String,
  var email: String,
  var phoneNumber: String,
  var address: String
)
