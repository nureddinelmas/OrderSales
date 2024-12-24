package com.nureddinelmas.onlinesales.viewModel

import com.nureddinelmas.onlinesales.models.Customer


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nureddinelmas.onlinesales.models.Order
import com.nureddinelmas.onlinesales.repository.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CustomerUiState(
	val isLoading: Boolean = false,
	val customer: List<Customer> = emptyList(),
	val error: String? = null
)

class CustomerViewModel(private val repository: Repository) : ViewModel() {
	private val _uiState = MutableStateFlow(CustomerUiState())
	val uiState: StateFlow<CustomerUiState> = _uiState.asStateFlow()
	
	init {
		loadCustomers()
	}
	
	private fun loadCustomers() {
		viewModelScope.launch {
			_uiState.value = CustomerUiState(isLoading = true)
			repository.getCustomers()
				.onSuccess { customer ->
					_uiState.value = CustomerUiState(customer = customer)
					
				}
				.onFailure { throwable ->
					_uiState.value = CustomerUiState(
						error =
						throwable.message ?: "Unknown error"
					)
				}
		}
	}
	
	fun addCustomer(customer: Customer) {
		viewModelScope.launch {
			repository.addCustomer(customer)
				.onSuccess {
					Log.d("!!!", "OK added customer")
					loadCustomers()
				}
				.onFailure { throwable ->
					Log.d("!!!", "NOT OK! Faiulre")
					_uiState.value = CustomerUiState(
						error =
						throwable.message ?: "Error adding order"
					)
				}
		}
	}
	
	
	fun deleteOrder(customerId: String) {
		viewModelScope.launch {
			repository.deleteOrder(customerId)
				.onSuccess {
					loadCustomers()
					Log.d("!!!", "OK deleted orders")
				}
				.onFailure { throwable ->
					_uiState.value =
						CustomerUiState(error = throwable.message ?: "Error deleting order")
				}
		}
	}
}