package com.example.restgarden.data.model.request

data class UserRequest(
  var name: String,
  var username: String,
  var password: String,
  var email: String,
  var phoneNumber: String,
  var address: String
)
