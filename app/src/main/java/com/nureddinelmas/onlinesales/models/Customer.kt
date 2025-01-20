package com.nureddinelmas.onlinesales.models

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class Customer(
	val customerId: String? = UUID.randomUUID().toString(),
	var customerName: String? = "",
	var customerAddress: String? = "",
	var customerCity: String? = "",
	var customerCountry:String? = "",
	var customerTel: String? = "",
	var customerEmail:String? = "",
	)