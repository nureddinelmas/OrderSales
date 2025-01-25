package com.nureddinelmas.onlinesales.helper

import com.nureddinelmas.onlinesales.models.Order
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale


fun Double.toSekFormat(currency: String): String {
	val symbols = DecimalFormatSymbols(Locale("sv", "SE")).apply {
		groupingSeparator = ' ' // Use a space for thousands separator
		decimalSeparator = ',' // Use a comma for the decimal separator
	}
	
	val formatter = DecimalFormat("#,##0.00", symbols)
	val formattedPrice = formatter.format(this)
	return "$formattedPrice $currency"
}

fun totalQuantity(orders: List<Order>): Double {
	var totalQuantity = 0.0
	orders.forEach {
		totalQuantity += it.totalQuantity()
	}
	return totalQuantity
}
