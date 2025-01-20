package com.nureddinelmas.onlinesales.models

import kotlinx.serialization.Serializable
import java.util.Date
import java.util.UUID

@Serializable
data class Order(
	var orderId: String? = UUID.randomUUID().toString(),
	var orderDate: Long = Date().time,
	val customerId: String? = "",
	var productList: List<Product> = listOf(),
	var shipping: Double = 0.0,
) {
	fun totalPrice(): Double {
		return productList.sumOf { it.productPrice.toDouble() * it.productQuantity } + shipping
	}
	
	fun totalQuantity(): Double {
		return productList.sumOf { it.productQuantity }
	}
}