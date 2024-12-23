package com.nureddinelmas.onlinesales.helper

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nureddinelmas.onlinesales.repository.Repository
import com.nureddinelmas.onlinesales.viewModel.OrderViewModel
import com.nureddinelmas.onlinesales.viewModel.ProductViewModel

class OrderViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
	override fun <T : ViewModel> create(modelClass: Class<T>): T {
		if (modelClass.isAssignableFrom(OrderViewModel::class.java)) {
			@Suppress("UNCHECKED_CAST")
			return OrderViewModel(repository = repository) as T
		}
		throw IllegalArgumentException("Unknown ViewModel class")
	}
}


class ProductViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
	override fun <T : ViewModel> create(modelClass: Class<T>): T {
		if (modelClass.isAssignableFrom(ProductViewModel::class.java)) {
			@Suppress("UNCHECKED_CAST")
			return ProductViewModel(repository = repository) as T
		}
		throw IllegalArgumentException("Unknown ViewModel class")
	}
}