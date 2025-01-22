package com.nureddinelmas.onlinesales.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nureddinelmas.onlinesales.models.Order
import com.nureddinelmas.onlinesales.repository.Repository
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class OrderUiState(
    val isLoading: Boolean = false,
    val orders: List<Order> = emptyList(),
    val error: String? = null
)

class OrderViewModel(private val repository: Repository) : ViewModel() {
    private val _uiState = MutableStateFlow(OrderUiState())
    val uiState: StateFlow<OrderUiState> = _uiState.asStateFlow()

    init {
        loadOrders()
    }

    private fun loadOrders() {
        viewModelScope.launch {
            _uiState.value = OrderUiState(isLoading = true)
            repository.getOrders()
                .onSuccess { orders ->
                    _uiState.value = OrderUiState(orders = orders)

                }
                .onFailure { throwable ->
                    _uiState.value = OrderUiState(
                        error =
                        throwable.message ?: "Unknown error"
                    )
                }
        }
    }

    fun addOrder(order: Order) {
        viewModelScope.launch {
            repository.addOrder(order)
                .onSuccess {
                    Log.d("!!!", "OK added orders")
                    loadOrders()
                }
                .onFailure { throwable ->
                    Log.d("!!!", "NOT OK! Faiulre")
                    _uiState.value = OrderUiState(
                        error =
                        throwable.message ?: "Error adding order"
                    )
                }
        }
    }

    fun updateOrder(order: Order) {
        viewModelScope.launch {
            repository.updateOrder(order)
                .onSuccess {
                    loadOrders()
                    Log.d("!!!", "OK updated orders")
                }
                .onFailure { throwable ->
                    _uiState.value = OrderUiState(
                        error =
                        throwable.message ?: "Error updating order"
                    )
                }
        }
    }
    
    fun deleteOrder(orderId: String) {
        viewModelScope.launch {
            repository.deleteOrder(orderId)
                .onSuccess {
                    loadOrders()
                    Log.d("!!!", "OK deleted orders")
                }
                .onFailure { throwable ->
                    _uiState.value =
                        OrderUiState(error = throwable.message ?: "Error deleting order")
                }
        }
    }
    
    fun checkCustomerExistInOrders(customerId: String): Boolean = run {
        _uiState.value.orders.forEach { order ->
            if (order.customerId == customerId) {
                Log.d("!!!", "Customer exist in orders")
                return@run true
            }
        }
        false
    }
    
    fun getTotalPrice(): Double {
        var total = 0.0
        onlyNotArkivedOrders().forEach { order ->
            total += order.totalPrice()
        }
        return total
    }
    
    fun onlyNotArkivedOrders(): List<Order> {
        return _uiState.value.orders.filter { !it.isArkived }
    }
}