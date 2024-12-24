package com.nureddinelmas.onlinesales.models

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import java.util.UUID

data class Order(
	var orderId: String? = UUID.randomUUID().toString(),
	val customer: Customer = Customer(),
	val productList: List<Product> = listOf(),
) {
	fun totalPrice(): Double {
		return productList.sumOf { it.productPrice.toDouble() * it.productQuantity }
	}
	
	fun totalQuantity(): Double {
		return productList.sumOf { it.productQuantity.toDouble() }
	}
}