package com.nureddinelmas.onlinesales.models

import android.graphics.Color
import kotlinx.serialization.Serializable
import java.util.Date
import java.util.UUID

@Serializable
data class Order(
	var orderId: String? = UUID.randomUUID().toString(),
	var orderDate: Long = Date().time,
	val customer: Customer? = null,
	var productList: List<Product> = listOf(),
	var shipping: Double = 0.0,
	var process: OrderProcess? = OrderProcess.ORDERED,
	var isArchived: Boolean = false
) {
	fun totalPrice(): Double {
		return productList.sumOf { it.productPrice.toDouble() * it.productQuantity } + shipping
	}
	
	fun totalQuantity(): Double {
		return productList.sumOf { it.productQuantity }
	}
}

enum class OrderProcess(val displayName : String) {
	ORDERED("Ordered"),
	PROCESSING("Processing"),
	SHIPPED("Shipped"),
	DELIVERED("Delivered"),
	CANCELLED("Cancelled")
}

fun getOrderProcessString(orderProcess: OrderProcess): String {
	return when (orderProcess) {
		OrderProcess.ORDERED -> "Ordered"
		OrderProcess.PROCESSING -> "Processing"
		OrderProcess.SHIPPED -> "Shipped"
		OrderProcess.DELIVERED -> "Delivered"
		OrderProcess.CANCELLED -> "Cancelled"
	}
}

fun getOrderProcessColor(orderProcess: OrderProcess): Int {
	return when (orderProcess) {
		OrderProcess.ORDERED -> Color.parseColor("#FFA500")
		OrderProcess.PROCESSING -> Color.parseColor("#FFD700")
		OrderProcess.SHIPPED -> Color.parseColor("#32CD32")
		OrderProcess.DELIVERED -> Color.parseColor("#ADD8E6")
		OrderProcess.CANCELLED -> Color.parseColor("#FFCCCC")
	}
}