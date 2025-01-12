package com.nureddinelmas.onlinesales.widgets.order

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.nureddinelmas.onlinesales.NAVIGATION_SCREEN_ORDER_LIST
import com.nureddinelmas.onlinesales.models.Order
import com.nureddinelmas.onlinesales.models.Product
import com.nureddinelmas.onlinesales.viewModel.OrderViewModel
import com.nureddinelmas.onlinesales.viewModel.ProductViewModel
import com.nureddinelmas.onlinesales.widgets.product.ProductDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderDetailsScreen(
	order: Order,
	productViewModel: ProductViewModel,
	orderViewModel: OrderViewModel,
	navController: NavController
) {
	var showProductsDialog by remember { mutableStateOf(false) }
	var showShippingDialog by remember { mutableStateOf(false) }
	var currentOrder by remember { mutableStateOf(order) }
	
	Log.w("!!!", "OrderDetailsScreen Order: ${currentOrder.shipping}")
	
	Column {
		TopAppBar(
			title = { Text("Order Details") },
			navigationIcon = {
				IconButton(onClick = { navController.navigate(NAVIGATION_SCREEN_ORDER_LIST) }) {
					Icon(Icons.Default.ArrowBack, contentDescription = "Back to Order List")
				}
			}
		)
		
		Card(
			modifier = Modifier
				.fillMaxWidth()
				.padding(8.dp),
			elevation = CardDefaults.cardElevation(4.dp)
		) {
			Column(modifier = Modifier.padding(16.dp)) {
				Text(
					text = "Customer :",
					style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
					modifier = Modifier.padding(vertical = 8.dp)
				)
				Text(text = "Name:   ${currentOrder.customer.customerName}")
				Text(text = "Address:   ${currentOrder.customer.customerAddress}")
				Text(text = "City:   ${currentOrder.customer.customerCity}")
				Text(text = "Country:   ${currentOrder.customer.customerCountry}")
				Text(text = "Tel:   ${currentOrder.customer.customerTel}")
				Text(text = "Email:   ${currentOrder.customer.customerEmail}")
				
				
				HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), thickness = 2.dp)
				Row(
					modifier = Modifier
						.fillMaxWidth()
						.padding(vertical = 8.dp, horizontal = 4.dp),
					horizontalArrangement = Arrangement.SpaceBetween
				) {
					Text(
						text = "Product Name",
						modifier = Modifier
							.weight(1f)
							.padding(vertical = 4.dp),
						style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Bold)
					)
					Text(
						text = "Quantity",
						modifier = Modifier
							.weight(1f)
							.padding(vertical = 4.dp),
						textAlign = TextAlign.Center,
						style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Bold)
					)
					Text(
						text = "Price",
						modifier = Modifier
							.weight(1f)
							.padding(vertical = 4.dp),
						style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Bold)
					)
				}
				currentOrder.productList.forEach { product ->
					
					if (product.productQuantity != 0.0) Row(
						modifier = Modifier.fillMaxWidth(),
						horizontalArrangement = Arrangement.SpaceBetween
					) {
						Text(
							text = product.productName.uppercase(),
							modifier = Modifier
								.weight(1f)
								.padding(vertical = 4.dp)
						)
						Text(
							text = product.productQuantity.toString() + " kg",
							modifier = Modifier
								.weight(1f)
								.padding(vertical = 4.dp),
							textAlign = TextAlign.Center
						)
						Text(
							text = product.totalPrice()
								.toString() + " ${product.productCurrency.uppercase()}",
							modifier = Modifier
								.weight(1f)
								.padding(vertical = 4.dp)
						)
						
					}
				}
				Row(
					modifier = Modifier
						.fillMaxWidth()
						.padding(vertical = 8.dp, horizontal = 4.dp),
					horizontalArrangement = Arrangement.End
				) {
					Text(
						text = "Shipping: ${
							currentOrder.shipping.toString().uppercase()
						} ${currentOrder.productList[0].productCurrency.uppercase()}",
						style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold),
						textAlign = TextAlign.Center
					)
				}
				Row(
					modifier = Modifier
						.fillMaxWidth()
						.padding(vertical = 8.dp, horizontal = 4.dp),
					horizontalArrangement = Arrangement.End
				) {
					Text(
						text = "Total Price: ${currentOrder.totalPrice()} ${currentOrder.productList[0].productCurrency.uppercase()}",
						style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold),
						textAlign = TextAlign.Center
					)
				}
				Row(
					modifier = Modifier.fillMaxWidth(),
					horizontalArrangement = Arrangement.SpaceBetween,
				) {
					Button(onClick = {
						showShippingDialog = true
					}) {
						Text("Update Shipping")
					}
					
					AddButton(text = "Update Order") {
						showProductsDialog = true
					}
				}
				if (showShippingDialog) ShippingDialog(
					order = currentOrder,
					onDismiss = { showShippingDialog = false },
					onSave = { shippingCost ->
						val updatedOrder = currentOrder.copy(shipping = shippingCost)
						currentOrder = updatedOrder
						orderViewModel.updateOrder(updatedOrder)
					}
				)
				if (showProductsDialog) ProductDialog(
					viewModel = productViewModel,
					onDismiss = { showProductsDialog = false },
					selectedProducts = currentOrder.productList,
					onSave = { list ->
						currentOrder.productList = list as SnapshotStateList<Product>
						orderViewModel.updateOrder(currentOrder)
						Log.d("!!!", "OrderDetailsScreen: ${order.productList}")
					}
				)
			}
		}
	}
}