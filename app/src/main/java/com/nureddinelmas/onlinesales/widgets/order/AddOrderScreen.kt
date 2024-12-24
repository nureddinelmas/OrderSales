package com.nureddinelmas.onlinesales.widgets.order

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.nureddinelmas.onlinesales.NAVIGATION_SCREEN_ORDER_LIST
import com.nureddinelmas.onlinesales.models.Customer
import com.nureddinelmas.onlinesales.models.Order
import com.nureddinelmas.onlinesales.models.Product
import com.nureddinelmas.onlinesales.viewModel.CustomerViewModel
import com.nureddinelmas.onlinesales.viewModel.OrderViewModel
import com.nureddinelmas.onlinesales.viewModel.ProductViewModel


@Composable
fun AddOrderScreen(
	productViewModel: ProductViewModel,
	navController: NavController,
	orderViewModel: OrderViewModel,
	customerViewModel: CustomerViewModel
) {
	var showProductsDialog by remember { mutableStateOf(false) }
	var showCustomerDialog by remember { mutableStateOf(false) }
	val selectedProducts = remember { mutableStateListOf<Product>() }
	val selectedCustomer = remember { Customer() }
	Scaffold { padding ->
		Column(modifier = Modifier.padding(padding)) {
			Card(
				modifier = Modifier
					.fillMaxWidth()
					.padding(8.dp),
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
							text = selectedCustomer.customerName ?: "",
							modifier = Modifier.weight(1f)
						)
						Text(
							text = selectedCustomer.customerTel ?: "",
							modifier = Modifier.weight(1f)
						)
					}
				}
				AddButton(text = "Add Customer") {
					showCustomerDialog = true
				}
			}
			
			if (showCustomerDialog) {
				CustomerDialog(
					viewModel = customerViewModel,
					onDismiss = { showCustomerDialog = false },
					selectedCustomer = selectedCustomer
				)
			}
			
			Card(
				modifier = Modifier
					.fillMaxWidth()
					.padding(8.dp),
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
			
			if (showProductsDialog) {
				ProductDialog(
					viewModel = productViewModel,
					onDismiss = { showProductsDialog = false },
					selectedProducts = selectedProducts
				)
			}
			Row(
				modifier = Modifier
					.fillMaxWidth()
					.padding(2.dp),
				horizontalArrangement = Arrangement.Center
			) {
				Button(
					enabled = selectedCustomer.customerName != null && selectedProducts.isNotEmpty(),
					onClick = {
						val order =
							Order(customer = selectedCustomer, productList = selectedProducts)
						orderViewModel.addOrder(order)
						navController.navigate(NAVIGATION_SCREEN_ORDER_LIST)
					},
					modifier = Modifier
						.padding(5.dp)
						.fillMaxWidth()
				) { Text("Save", style = TextStyle(fontSize = 20.sp)) }
			}
			
		}
	}
	
}

@Composable
fun CustomerDialog(
	viewModel: CustomerViewModel,
	onDismiss: () -> Unit,
	selectedCustomer: Customer = Customer()
) {
	val customer by viewModel.uiState.collectAsState()
	Dialog(onDismissRequest = onDismiss) {
		Surface(
			shape = MaterialTheme.shapes.extraLarge,
		) {
			Column {
				LazyColumn {
					items(customer.customer) { customer ->
						Row(
							modifier = Modifier
								.fillMaxWidth()
								.padding(5.dp),
							verticalAlignment = Alignment.CenterVertically,
							horizontalArrangement = Arrangement.SpaceBetween
						) {
							Text(text = customer.customerName ?: "")
							Button(
								onClick = {
									selectedCustomer.customerName = customer.customerName
									selectedCustomer.customerTel = customer.customerTel
									selectedCustomer.customerAddress = customer.customerAddress
									selectedCustomer.customerCity = customer.customerCity
									selectedCustomer.customerCountry = customer.customerCountry
									selectedCustomer.customerEmail = customer.customerEmail
									onDismiss()
								},
								modifier = Modifier
									.padding(5.dp)
							) { Text("Add") }
						}
					}
				}
			}
		}
	}
	
}


@Composable
fun ProductDialog(
	viewModel: ProductViewModel,
	onDismiss: () -> Unit,
	selectedProducts: MutableList<Product>,
) {
	val products by viewModel.uiState.collectAsState()
	
	Dialog(onDismissRequest = onDismiss) {
		Surface(
			modifier = Modifier
				.fillMaxSize()
				.fillMaxHeight()
				.fillMaxWidth(),
			shape = MaterialTheme.shapes.extraLarge,
		) {
			Column {
				LazyColumn {
					items(products.products) { product ->
						ProductSearchItem(
							product = product,
							selectedProducts
						)
					}
				}
				Button(
					enabled = selectedProducts.isNotEmpty() || products.products.isEmpty(),
					onClick = {
						onDismiss()
					},
					modifier = Modifier
						.height(50.dp)
						.width(100.dp)
						.padding(5.dp)
				) { Text("Ok") }
				
			}
		}
	}
}


@Composable
fun ProductSearchItem(
	product: Product,
	selectedProducts: MutableList<Product>
) {
	var quantity by remember { mutableFloatStateOf(0f) }
	var isEnable = remember { mutableStateOf(false) }
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
				if (quantity > 0) quantity -= 0.5f
			}) {
				Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "Decrease Quantity")
			}
			
			Text(text = quantity.toString(), modifier = Modifier.padding(horizontal = 16.dp))
			
			IconButton(onClick = { quantity += 0.5f }) {
				Icon(Icons.Default.KeyboardArrowRight, contentDescription = "Increase Quantity")
			}
			
			Button(
				enabled = !isEnable.value,
				onClick = {
					if (quantity > 0) {
						val productList = Product(
							productName = product.productName,
							productQuantity = quantity.toDouble(),
							productPrice = product.productPrice,
							productCurrency = product.productCurrency,
							productComment = product.productComment
						)
						selectedProducts.add(productList)
					}
					if (quantity > 0) {
						isEnable.value = true
					}
					
				},
				modifier = Modifier
					.padding(5.dp)
			) { Text("Add") }
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