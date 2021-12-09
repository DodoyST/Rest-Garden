package com.example.restgarden.util

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import java.text.DecimalFormat

fun Context.hideKeyboard(view: View) {
  val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
  inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Double.currencyFormat(): String {
  val decimalFormat = DecimalFormat("Rp #,###.00")
  return decimalFormat.format(this)
}
