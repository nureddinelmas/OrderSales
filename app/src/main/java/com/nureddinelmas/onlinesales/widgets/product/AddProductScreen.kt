package com.nureddinelmas.onlinesales.widgets.product

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nureddinelmas.onlinesales.NAVIGATION_SCREEN_CUSTOMER_LIST
import com.nureddinelmas.onlinesales.NAVIGATION_SCREEN_PRODUCT_LIST
import com.nureddinelmas.onlinesales.models.Product
import com.nureddinelmas.onlinesales.viewModel.ProductViewModel
import kotlin.reflect.KFunction1

@Composable
fun AddNewProductScreen(
	navController: NavController,
	product: Product,
	isUpdateProduct: Boolean,
	productViewModel: ProductViewModel,
	onSaveProduct: (Product) -> Unit
) {
	val productName = remember { mutableStateOf(product.productName) }
	val productPrice = remember { mutableStateOf(product.productPrice) }
	val productCurrency = remember { mutableStateOf(product.productCurrency) }
	val productComment = remember { mutableStateOf(product.productComment) }
	
	Column(
		modifier = Modifier
			.fillMaxSize()
			.padding(16.dp)
	) {
		OutlinedTextField(
			value = productName.value,
			onValueChange = { productName.value = it },
			label = { Text("Product Name") },
			modifier = Modifier.padding(vertical = 4.dp)
		)
		OutlinedTextField(
			value = productPrice.value,
			onValueChange = { productPrice.value = it },
			label = { Text("Product Price") },
			modifier = Modifier.padding(vertical = 4.dp)
		)
		OutlinedTextField(
			value = productCurrency.value,
			onValueChange = { productCurrency.value = it },
			label = { Text("Product Currency") },
			modifier = Modifier.padding(vertical = 4.dp)
		)
		OutlinedTextField(
			value = productComment.value,
			onValueChange = { productComment.value = it },
			label = { Text("Product Comment") },
			modifier = Modifier.padding(vertical = 4.dp)
		)
		Row(
			modifier = Modifier
				.padding(vertical = 8.dp)
				.fillMaxWidth(),
			horizontalArrangement = Arrangement.SpaceBetween
		) {
			Button(
				onClick = {
					navController.navigate(NAVIGATION_SCREEN_PRODUCT_LIST)
				},
				modifier = Modifier
					.padding(8.dp)
					.weight(1f)
			) {
				Text("Cancel")
			}
			Button(
				onClick = {
					val newProduct = Product(
						productId = if (isUpdateProduct) product.productId else null,
						productName = productName.value,
						productPrice = productPrice.value,
						productCurrency = productCurrency.value,
						productComment = productComment.value
					)
					
					if (isUpdateProduct) {
						productViewModel.updateProduct(newProduct)
						navController.navigate(NAVIGATION_SCREEN_PRODUCT_LIST)
					} else {
						onSaveProduct(newProduct)
						navController.navigate(NAVIGATION_SCREEN_PRODUCT_LIST)
					}
				},
				modifier = Modifier
					.padding(vertical = 8.dp)
					.weight(1f)
			) {
				Text(if (isUpdateProduct) "Update Product" else "Save Product")
			}
		}
		if (isUpdateProduct) {
			
			var showDialog by remember { mutableStateOf(false) }
			
			if (showDialog) {
				AlertDialog(
					onDismissRequest = { showDialog = false },
					title = { Text("Delete product") },
					text = { Text("Are you sure you want to delete this product?") },
					confirmButton = {
						TextButton(
							onClick = {
								productViewModel.deleteProduct(product)
								navController.navigate(NAVIGATION_SCREEN_PRODUCT_LIST)
								showDialog = false
							}
						) {
							Text("Yes")
						}
					},
					dismissButton = {
						TextButton(
							onClick = { showDialog = false }
						) {
							Text("No")
						}
					}
				)
			}
			Button(
				onClick = {
					showDialog = true
				},
				modifier = Modifier
					.padding(2.dp)
					.fillMaxWidth()
			) {
				Text("Delete Customer")
			}
		}
	}
}