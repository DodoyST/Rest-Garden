package com.example.restgarden.data.model


data class Grave(
  var id: String,
  var name: String,
  var availableSlots: Int,
  var price: Double,
  var address: String,
  var phoneNumber: String,
  var type: String,
  var description: String,
  var image: String
)
