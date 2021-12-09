package com.example.restgarden.data.model

data class Booking(
  var id: String,
  var graveName: String,
  var graveAddress: String,
  var gravePrice: Double,
  var status: String,
  var totalPayment: Double,
  var description: String,
  var graveId: String,
  var userId: String,
  var totalSlot: Int
)
