package com.example.restgarden.data.model

data class Booking(
  var id: String,
  var description: String,
  var graveId: String,
  var userId: String,
  var totalSlot: Int
)
