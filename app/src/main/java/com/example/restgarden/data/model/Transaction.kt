package com.example.restgarden.data.model

import java.sql.Timestamp

data class Transaction(
  var id: String,
  var graveName: String,
  var graveAddress: String,
  var image: String,
  var gravePrice: Double,
  var totalSlot: Int,
  var status: String,
  var expiredDate: Timestamp,
  var totalPayment: Double,
  var description: String,
)
