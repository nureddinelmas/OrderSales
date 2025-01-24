package com.nureddinelmas.onlinesales.helper

import com.nureddinelmas.onlinesales.models.Order
import java.text.DecimalFormat

fun Double.toSekFormat(currency: String): String {
	// Format the double value to two decimal places, and replace dot with a comma
	val formatter = DecimalFormat("##0.00")
	val formattedPrice = formatter.format(this).replace('.', ',') // replace dot with comma
	return "$formattedPrice $currency"
}

fun totalQuantity(orders: List<Order>): Double {
	var totalQuantity = 0.0
	orders.forEach {
		totalQuantity += it.totalQuantity()
	}
	return totalQuantity
}
