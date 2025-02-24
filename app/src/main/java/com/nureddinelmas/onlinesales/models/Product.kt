package com.nureddinelmas.onlinesales.models

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class Product(
	val productId: String? = UUID.randomUUID().toString(),
	val productName: String = "",
	val productPrice: String = "0",
	val productCurrency: String = "",
	val productComment: String = "",
	var productQuantity: Double = 0.0,
	var isPieces: Boolean = false
) {
    fun totalPrice(): Double {
        return productPrice.toDouble() * productQuantity
    }
	
	fun defineSortOfQuantity(): String {
		return if (isPieces) {
			"Adet"
		} else {
			"kg"
		}
	}
}