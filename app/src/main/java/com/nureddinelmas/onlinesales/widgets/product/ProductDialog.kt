package com.nureddinelmas.onlinesales.widgets.product

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.nureddinelmas.onlinesales.models.Product
import com.nureddinelmas.onlinesales.viewModel.ProductViewModel

@Composable
fun ProductDialog(
	viewModel: ProductViewModel,
	onDismiss: () -> Unit,
	selectedProducts: List<Product>,
	onSave: (List<Product>) -> Unit
) {
	val products by viewModel.uiState.collectAsState()
	val tempSelectedProducts = remember { mutableStateListOf<Product>().apply { addAll(selectedProducts) } }
	
	Dialog(onDismissRequest = onDismiss) {
		Surface(
			modifier = Modifier
				.fillMaxWidth()
				.wrapContentHeight(),
			shape = MaterialTheme.shapes.extraLarge,
		) {
			Column {
				LazyColumn {
					items(products.products) { product ->
						val existingProduct = tempSelectedProducts.find { it.productId == product.productId }
						val initialQuantity = existingProduct?.productQuantity ?: 0.0
						
						ProductItems(
							product = product,
							initialQuantity = initialQuantity,
							onQuantityChange = { quantity ->
								if (quantity > 0) {
									val updatedProduct = product.copy(productQuantity = quantity)
									if (existingProduct != null) {
										tempSelectedProducts[tempSelectedProducts.indexOf(existingProduct)] = updatedProduct
									} else {
										tempSelectedProducts.add(updatedProduct)
									}
								} else {
									tempSelectedProducts.remove(product)
								}
							}
						)
					}
				}
				Button(
					onClick = {
						onSave(tempSelectedProducts)
						onDismiss()
					},
					modifier = Modifier
						.padding(5.dp)
				) { Text("Save") }
			}
		}
	}
}

@Composable
fun ProductItems(
	product: Product,
	initialQuantity: Double,
	onQuantityChange: (Double) -> Unit
) {
	var quantity by remember { mutableStateOf(initialQuantity) }
	
	Row(
		modifier = Modifier
			.fillMaxWidth()
			.padding(5.dp),
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.SpaceBetween
	) {
		Text(text = product.productName)
		
		Row(
			verticalAlignment = Alignment.CenterVertically
		) {
			IconButton(onClick = {
				if (quantity > 0) quantity -= 0.5
				onQuantityChange(quantity)
			}) {
				Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "Decrease Quantity")
			}
			
			Text(text = quantity.toString(), modifier = Modifier.padding(horizontal = 16.dp))
			
			IconButton(onClick = {
				quantity += 0.5
				onQuantityChange(quantity)
			}) {
				Icon(Icons.Default.KeyboardArrowRight, contentDescription = "Increase Quantity")
			}
		}
	}
}