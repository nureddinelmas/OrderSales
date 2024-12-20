package com.nureddinelmas.onlinesales.viewModel

import androidx.activity.result.launch
import androidx.compose.runtime.Recomposer
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nureddinelmas.onlinesales.models.Order
import com.nureddinelmas.onlinesales.ui.theme.OrderRepository
import androidx.compose.runtime.State
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

class OrderListViewModel : ViewModel() {
    private val orderRepository = OrderRepository()
    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableSharedFlow<String?>()
    val errorMessage: SharedFlow<String?> = _errorMessage.asSharedFlow()


    init {
        loadOrders()
    }

    private fun loadOrders() {
        viewModelScope.launch {
            _isLoading.value = true
            runCatching { orderRepository.getOrders() }
                .onSuccess { _orders.value = it.getOrDefault(emptyList()) }
                .onFailure { _errorMessage.emit(it.message) }
            _isLoading.value = false
        }
    }


    fun addOrder(order: Order) {
        viewModelScope.launch {
            runCatching { orderRepository.addOrder(order) }
                .onSuccess { loadOrders() }
                .onFailure { _errorMessage.emit(it.message) }
        }
    }
}