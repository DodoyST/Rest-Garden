package com.example.restgarden.data.model

import java.sql.Timestamp

data class Transaction(
  var id: String,
  var graveName: String,
  var graveAddress: String,
  var image: String,
  var gravePrice: Double,
  var totalSlot: Int,
  var date: Timestamp,
  var totalPrice: Double,
  var description: String,
)
