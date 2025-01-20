package com.nureddinelmas.onlinesales.widgets.product

import android.content.pm.ModuleInfo
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
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
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
	val tempSelectedProducts =
		remember { mutableStateListOf<Product>().apply { addAll(selectedProducts) } }
	
	Column(
		modifier = Modifier
			.fillMaxWidth()
			.wrapContentHeight()
			.padding(5.dp)
	) {
		LazyColumn (
			modifier = Modifier
				.fillMaxWidth()
				.weight(8f)
		) {
			items(products.products) { product ->
				val existingProduct =
					tempSelectedProducts.find { it.productId == product.productId }
				val initialQuantity = existingProduct?.productQuantity ?: 0.0
				ProductItems(
					product = product,
					initialQuantity = initialQuantity,
					onQuantityChange = { quantity ->
						if (quantity > -1) {
							val updatedProduct = product.copy(productQuantity = quantity)
							if (existingProduct != null) {
								tempSelectedProducts[tempSelectedProducts.indexOf(existingProduct)] =
									updatedProduct
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
		Row (
			horizontalArrangement= Arrangement.SpaceBetween
		) {
			Button(
				onClick = {
					onDismiss()
				},
				modifier = Modifier
					.padding(vertical = 2.dp, horizontal = 9.dp)
					.fillMaxWidth()
					.weight(0.7f)
			) { Text("Cancel") }
			Button(
				onClick = {
					onSave(tempSelectedProducts)
					onDismiss()
				},
				modifier = Modifier
					.padding(vertical = 2.dp)
					.fillMaxWidth()
					.weight(0.7f)
			) { Text("Save") }
		}
	}
}


@Composable
fun ProductItems(
	product: Product,
	initialQuantity: Double,
	onQuantityChange: (Double) -> Unit
) {
	var quantity by remember { mutableDoubleStateOf(initialQuantity) }
	
	Row(
		modifier = Modifier
			.fillMaxWidth()
			.padding(5.dp),
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.SpaceBetween
	) {
		Text(text = product.productName, modifier = Modifier.weight(5f))
		
		Row(
			verticalAlignment = Alignment.CenterVertically
			, modifier = Modifier.weight(3f)
		) {
			IconButton(onClick = {
				if (quantity > 0) quantity -= 0.5
				onQuantityChange(quantity)
			}) {
				Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Decrease Quantity")
			}
			
			Text(text = quantity.toString(), modifier = Modifier.padding(horizontal = 16.dp))
			
			IconButton(onClick = {
				quantity += 0.5
				onQuantityChange(quantity)
			}) {
				Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Increase Quantity")
			}
		}
	}
}