package com.nureddinelmas.onlinesales.widgets.order

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.nureddinelmas.onlinesales.NAVIGATION_SCREEN_ORDER_LIST
import com.nureddinelmas.onlinesales.models.Customer
import com.nureddinelmas.onlinesales.models.Order
import com.nureddinelmas.onlinesales.models.OrderProcess
import com.nureddinelmas.onlinesales.models.Product
import com.nureddinelmas.onlinesales.viewModel.CustomerViewModel
import com.nureddinelmas.onlinesales.viewModel.OrderViewModel
import com.nureddinelmas.onlinesales.viewModel.ProductViewModel
import com.nureddinelmas.onlinesales.widgets.customer.CustomerDialog
import com.nureddinelmas.onlinesales.widgets.product.ProductDialog


@Composable
fun AddOrderScreen(
	productViewModel: ProductViewModel,
	navController: NavController,
	orderViewModel: OrderViewModel,
	customerViewModel: CustomerViewModel
) {
	var showProductsDialog by remember { mutableStateOf(false) }
	var showCustomerDialog by remember { mutableStateOf(false) }
	var selectedProducts = remember { mutableStateListOf<Product>() }
	val selectedCustomer = remember { mutableStateOf(Customer()) }
	Scaffold { padding ->
		if (!showProductsDialog) Column(modifier = Modifier.padding(padding)) {
			Card(
				modifier = Modifier
					.fillMaxWidth()
					.padding(8.dp)
					.weight(2.5f),
				elevation = CardDefaults.cardElevation(4.dp)
			) {
				
				Column(modifier = Modifier.padding(16.dp)) {
					Row(
						modifier = Modifier.fillMaxWidth(),
						horizontalArrangement = Arrangement.SpaceBetween
					) {
						Text(text = "Customer Name", modifier = Modifier.weight(1f))
						Text(text = "Mobile number", modifier = Modifier.weight(1f))
					}
					Row(
						modifier = Modifier.fillMaxWidth(),
						horizontalArrangement = Arrangement.SpaceBetween
					) {
						Text(
							text = selectedCustomer.value.customerName
								?: "",
							modifier = Modifier.weight(1f)
						)
					}
				}
				AddButton(text = "Add Customer") {
					showCustomerDialog = true
				}
			}
			Card(
				modifier = Modifier
					.fillMaxWidth()
					.padding(8.dp)
					.weight(8f),
				elevation = CardDefaults.cardElevation(4.dp)
			) {
				Column(modifier = Modifier.padding(8.dp)) {
					Row(
						modifier = Modifier.fillMaxWidth(),
						horizontalArrangement = Arrangement.SpaceBetween
					) {
						Text(text = "Product Name", modifier = Modifier.weight(1f))
						Text(text = "Quantity", modifier = Modifier.weight(1f))
					}
					LazyColumn {
						items(selectedProducts) { product ->
							Row(
								modifier = Modifier.fillMaxWidth(),
								horizontalArrangement = Arrangement.SpaceBetween
							) {
								Text(
									text = product.productName,
									modifier = Modifier.weight(1f)
								)
								Text(
									text = product.productQuantity.toString(),
									modifier = Modifier.weight(1f)
								)
							}
						}
					}
				}
				
				AddButton(text = "Add Product") {
					showProductsDialog = true
				}
			}
			
			Row(
				modifier = Modifier
					.fillMaxWidth()
					.padding(2.dp)
					.weight(1f),
				horizontalArrangement = Arrangement.SpaceBetween
			) {
				Button(
					onClick = {
						navController.navigate(NAVIGATION_SCREEN_ORDER_LIST)
					},
					modifier = Modifier
						.padding(5.dp)
						.weight(1f)
				) { Text("Cancel", style = TextStyle(fontSize = 20.sp)) }
				Button(
					enabled = selectedProducts.isNotEmpty(),
					onClick = {
						val order =
							Order(
								customer = selectedCustomer.value,
								productList = selectedProducts,
								orderDate = System.currentTimeMillis(),
								process = OrderProcess.ORDERED
							)
						orderViewModel.addOrder(order)
						navController.navigate(NAVIGATION_SCREEN_ORDER_LIST)
					},
					modifier = Modifier
						.padding(5.dp)
						.weight(1f)
				) { Text("Save", style = TextStyle(fontSize = 20.sp)) }
			}
			
		}
		
		if (showProductsDialog) {
			ProductDialog(
				viewModel = productViewModel,
				onDismiss = { showProductsDialog = false },
				selectedProducts = selectedProducts,
				onSave = { selectedProducts = it as SnapshotStateList<Product> }
			)
		}
		
		if (showCustomerDialog) {
			CustomerDialog(
				viewModel = customerViewModel,
				onDismiss = { showCustomerDialog = false },
				onSelectedCustomer = { selectedCustomer.value = it }
			)
		}
	}
	
}

@Composable
fun AddButton(
	text: String,
	onClick: () -> Unit
) {
	Row(
		modifier = Modifier
			.fillMaxWidth()
			.padding(2.dp),
		horizontalArrangement = Arrangement.End
	) {
		Button(
			onClick = onClick
		) {
			Text(text)
		}
	}
}