package com.nureddinelmas.onlinesales.models

import java.util.UUID

data class Order(
	var orderId: String? = UUID.randomUUID().toString(),
	val customer: Customer? = null,
	val productList: List<Product> = listOf(),
)