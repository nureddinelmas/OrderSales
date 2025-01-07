package com.nureddinelmas.onlinesales.widgets.order

import androidx.compose.animation.core.copy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.nureddinelmas.onlinesales.models.Order
import com.nureddinelmas.onlinesales.models.Product
import kotlin.text.toDoubleOrNull
import kotlin.text.toIntOrNull

@Composable
fun UpdateOrderScreen(order: Order, onOrderUpdated: (Order) -> Unit) {
	var updatedOrder by remember { mutableStateOf(order) }
	
	LazyColumn {
		items(updatedOrder.productList) { product ->
			ProductCard(product) { updatedProduct ->
				// Update the product in the list
				updatedOrder = updatedOrder.copy(
					productList = updatedOrder.productList.map {
						if (it.productId == updatedProduct.productId) updatedProduct else it
					}
				)
			}
		}
		item {
			Button(onClick = {
				// Add a new product to the list
				updatedOrder = updatedOrder.copy(
					productList = updatedOrder.productList + Product() // Assuming Product() creates a new empty product
				)
			}) {
				Text("Add Product")
			}
		}
	}
	
	Button(onClick = { onOrderUpdated(updatedOrder) }) {
		Text("Update Order")
	}
}

@Composable
fun ProductCard(product: Product, onProductUpdated: (Product) -> Unit) {
	var productName by remember { mutableStateOf(product.productName) }
	var productQuantity by remember { mutableStateOf(product.productQuantity) }
	var productPrice by remember { mutableStateOf(product.productPrice) }
	
	Card(
		modifier = Modifier
			.fillMaxWidth()
			.padding(8.dp)
	) {
		Column(
			modifier = Modifier
				.padding(16.dp)
		) {
			OutlinedTextField(
				enabled = false,
				value = productName,
				onValueChange = { productName = it },
				label = { Text("Product Name") }
			)
			OutlinedTextField(
				value = productQuantity.toString(),
				onValueChange = { productQuantity = (it.toIntOrNull() ?: 0).toDouble() },
				label = { Text("Quantity") },
				keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
			)
			OutlinedTextField(
				enabled = false,
				value = productPrice.toString(),
				onValueChange = { productPrice = (it.toDoubleOrNull() ?: 0.0).toString() },
				label = { Text("Price") },
				keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
			)
			// Add other product details as needed
			
			Button(onClick = {
				onProductUpdated(
					product.copy(
						productName = productName,
						productQuantity = productQuantity,
						productPrice = productPrice
						// Update other product properties as needed
					)
				)
			}) {
				Text("Update Product")
			}
			
			// Add a delete button if needed
			Button(onClick = { /* Handle product deletion */ }) {
				Text("Delete")
			}
		}
	}
}