package com.example.restgarden.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import java.sql.Timestamp
import java.text.DecimalFormat
import java.text.SimpleDateFormat

fun Context.hideKeyboard(view: View) {
  val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
  inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Double.currencyFormat(): String {
  val decimalFormat = DecimalFormat("Rp #,###.00")
  return decimalFormat.format(this)
}

@SuppressLint("SimpleDateFormat")
fun Long.toDate(): String {
  val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
  return simpleDateFormat.format(this)
}

@SuppressLint("SimpleDateFormat")
fun Timestamp.dateFormat(): String {
  val simpleDateFormat = SimpleDateFormat("dd MMM yyyy")
  return simpleDateFormat.format(this)
}

@SuppressLint("SimpleDateFormat")
fun Timestamp.timestampFormat(): String {
  val simpleDateFormat = SimpleDateFormat("dd MMMM yyyy, HH:mm")
  return simpleDateFormat.format(this)
}
