package com.nureddinelmas.onlinesales.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nureddinelmas.onlinesales.models.Product
import com.nureddinelmas.onlinesales.repository.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


data class ProductUiState(
    val isLoading: Boolean = false,
    val products: List<Product> = emptyList(),
    val error: String? = null
)


class ProductViewModel(private val repository: Repository) : ViewModel() {
    private val _uiState = MutableStateFlow(ProductUiState())
    val uiState: StateFlow<ProductUiState> = _uiState.asStateFlow()

    init {
        loadProducts()
    }

    private fun loadProducts() {
        viewModelScope.launch {
            _uiState.value = ProductUiState(isLoading = true)
            repository.getProducts()
                .onSuccess { products ->
                    _uiState.value = ProductUiState(products = products)
                    Log.d("!!!", "OK loaded products : $products")

                }
                .onFailure { throwable ->
                    _uiState.value = ProductUiState(
                        error =
                        throwable.message ?: "Unknown error"
                    )
                    Log.d("!!!", "Failed loaded products : ${throwable.message}")
                }
        }
    }


    fun addProduct(product: Product) {
        viewModelScope.launch {
            repository.addProduct(product)
                .onSuccess {
                    Log.d("!!!", "OK added orders")
                    loadProducts()
                }
                .onFailure { throwable ->
                    Log.d("!!!", "NOT OK! Faiulre")
                    _uiState.value = ProductUiState(
                        error =
                        throwable.message ?: "Error adding order"
                    )
                }
        }
    }

    fun updateProduct(product: Product) {
        viewModelScope.launch {
            repository.updateProduct(product)
                .onSuccess {
                    loadProducts()
                    Log.d("!!!", "OK updated orders")
                }
                .onFailure { throwable ->
                    _uiState.value = ProductUiState(
                        error =
                        throwable.message ?: "Error updating order"
                    )
                }
        }
    }
    fun deleteProduct(product: Product) {
        viewModelScope.launch {
            repository.deleteProduct(product.productId!!)
                .onSuccess {
                    loadProducts()
                    Log.d("!!!", "OK deleted orders")
                }
                .onFailure { throwable ->
                    _uiState.value =
                        ProductUiState(error = throwable.message ?: "Error deleting order")
                }
        }
    }
    
    fun getOnlyHavePriceProducts(): List<Product> {
        return _uiState.value.products.filter { it.productPrice != "0" }
    }
}