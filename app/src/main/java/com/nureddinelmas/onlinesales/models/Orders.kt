package com.nureddinelmas.onlinesales.models

import java.util.UUID

data class Order(
	var orderId: String? = UUID.randomUUID().toString(),
	val customerId: String = "",
	val orderQuantity: Int = 0,
	val productId: Int = 2
)