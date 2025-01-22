package com.nureddinelmas.onlinesales.widgets.product


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.nureddinelmas.onlinesales.models.Product
import com.nureddinelmas.onlinesales.viewModel.ProductViewModel
import java.util.UUID

@Composable
fun ProductListScreen(productViewModel: ProductViewModel, navController: NavController) {
	val uiState by productViewModel.uiState.collectAsState()
	
	var selectedProduct by remember { mutableStateOf(Product()) }
	
	var showProductDialog by remember { mutableStateOf(false) }
	
	when {
		uiState.isLoading -> {
			Box(
				modifier = Modifier.fillMaxSize(),
				contentAlignment = Alignment.Center
			) {
				CircularProgressIndicator()
			}
		}
		
		(uiState.error != null) -> {
			Box(
				modifier = Modifier.fillMaxSize(),
				contentAlignment = Alignment.Center
			) {
				Text(text = "Error: ${uiState.error}")
			}
		}
		
		uiState.products.isNotEmpty() -> {
			if (!showProductDialog) {
				Card(
					modifier = Modifier
						.fillMaxSize()
						.padding(8.dp)
				) {
					Column(
						modifier = Modifier.padding(vertical = 8.dp, horizontal = 8.dp)
					) {
						Row {
							Text(
								text = "Product name",
								modifier = Modifier
									.padding(vertical = 4.dp, horizontal = 8.dp)
									.weight(1f),
								textAlign = TextAlign.Start
							)
							Text(
								text = "Price",
								modifier = Modifier
									.padding(vertical = 4.dp)
									.weight(1f),
								textAlign = TextAlign.Center
							)
							Text(
								text = "Comment",
								modifier = Modifier
									.padding(vertical = 4.dp)
									.weight(1f)
							)
						}
						LazyColumn(
							modifier = Modifier
								.fillMaxSize()
								.background(color = Color(0xFFF0F0F0))
						) {
							items(
								items = uiState.products,
								key = { UUID.randomUUID().toString() }) { product ->
								
								ProductItem(product) {
									selectedProduct = product
									showProductDialog = true
								}
								
								
							}
						}
					}
				}
			}
			
		}
		
		uiState.products.isEmpty() -> {
			Box(
				modifier = Modifier.fillMaxSize(),
				contentAlignment = Alignment.Center
			) {
				Text(text = "No Products Found", style = TextStyle(fontSize = 20.sp))
			}
		}
	}
	
	if (showProductDialog) AddNewProductScreen(
		product = selectedProduct,
		productViewModel = productViewModel,
		isUpdateProduct = true,
		navController = navController,
		onSaveProduct = productViewModel::updateProduct,
	)
}

@Composable
fun ProductItem(product: Product, onClick: () -> Unit) {
	
	Column(modifier = Modifier
		.padding(vertical = 8.dp, horizontal = 8.dp)
		.clickable {
			onClick()
		}) {
		Row {
			Text(
				text = product.productName,
				modifier = Modifier
					.padding(vertical = 4.dp)
					.weight(1f),
				textAlign = TextAlign.Start
			)
			Text(
				text = "${product.productPrice} ${product.productCurrency.uppercase()}",
				modifier = Modifier
					.padding(vertical = 4.dp)
					.weight(1f),
				textAlign = TextAlign.Center
			)
			Text(
				text = product.productComment,
				modifier = Modifier
					.padding(vertical = 4.dp)
					.weight(1f)
			)
		}
		
		HorizontalDivider(
			thickness = 1.dp,
			color = Color.Gray,
			modifier = Modifier
				.padding(vertical = 8.dp)
				.weight(2f)
		)
	}
}
