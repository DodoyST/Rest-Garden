package com.example.restgarden.data.model.request

data class BookingTransactionRequest(
  var description: String,
  var graveId: String,
  var userId: String,
  var totalSlot: Int
)
