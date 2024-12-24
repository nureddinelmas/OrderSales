package com.nureddinelmas.onlinesales.models

import java.util.UUID

data class Customer(
	val customerId: String? = UUID.randomUUID().toString(),
	var customerName: String? = "",
	var customerAddress: String? = "",
	var customerCity: String? = "",
	var customerCountry:String? = "",
	var customerTel: String? = "",
	var customerEmail:String? = "",
	)