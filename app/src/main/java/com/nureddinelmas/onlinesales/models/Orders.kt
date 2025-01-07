package com.nureddinelmas.onlinesales.models

import java.util.Date
import java.util.UUID

data class Order(
	var orderId: String? = UUID.randomUUID().toString(),
	var orderDate: Long = Date().time,
	val customer: Customer = Customer(),
	var productList: List<Product> = listOf(),
) {
	fun totalPrice(): Double {
		return productList.sumOf { it.productPrice.toDouble() * it.productQuantity }
	}
	
	fun totalQuantity(): Double {
		return productList.sumOf { it.productQuantity.toDouble() }
	}
}