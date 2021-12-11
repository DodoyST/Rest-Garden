package com.example.restgarden.data.model

data class Transaction(
  var id: String,
  var graveName: String,
  var graveAddress: String,
  var gravePrice: Double,
  var status: String,
  var totalPayment: Double,
  var expiredDate: Long,
  var description: String,
  var graveId: String,
  var userId: String,
  var totalSlot: Int
)
