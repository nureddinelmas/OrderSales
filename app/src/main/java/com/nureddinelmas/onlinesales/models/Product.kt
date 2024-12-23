package com.nureddinelmas.onlinesales.models

import java.util.UUID

data class Product(
    val productId: String? = UUID.randomUUID().toString(),
    val productName: String = "",
    val productPrice: String = "",
    val productCurrency: String = "",
    val productComment: String = "",
    val productQuantity: Int = 0
)
