package com.nureddinelmas.onlinesales.models

data class Order(
    val orderId: Int = 1,
    val customerId: String = "",
    val orderQuantity: Int = 0,
    val productId: Int =2
)