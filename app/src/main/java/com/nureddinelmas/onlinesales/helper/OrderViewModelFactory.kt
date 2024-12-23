package com.nureddinelmas.onlinesales.helper

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nureddinelmas.onlinesales.OrderRepository
import com.nureddinelmas.onlinesales.viewModel.OrderViewModel

class OrderViewModelFactory(private val repository: OrderRepository) : ViewModelProvider.Factory {
	override fun <T : ViewModel> create(modelClass: Class<T>): T {
		if (modelClass.isAssignableFrom(OrderViewModel::class.java)) {
			@Suppress("UNCHECKED_CAST")
			return OrderViewModel(orderRepository = repository) as T
		}
		throw IllegalArgumentException("Unknown ViewModel class")
	}
}