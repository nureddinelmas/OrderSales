package com.nureddinelmas.onlinesales.widgets.order

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.nureddinelmas.onlinesales.NAVIGATION_SCREEN_ORDER_LIST
import com.nureddinelmas.onlinesales.print.createAndShareOrderPdf
import com.nureddinelmas.onlinesales.helper.toSekFormat
import com.nureddinelmas.onlinesales.models.Order
import com.nureddinelmas.onlinesales.models.Product
import com.nureddinelmas.onlinesales.models.getOrderProcessColor
import com.nureddinelmas.onlinesales.viewModel.CustomerViewModel
import com.nureddinelmas.onlinesales.viewModel.OrderViewModel
import com.nureddinelmas.onlinesales.viewModel.ProductViewModel
import com.nureddinelmas.onlinesales.widgets.product.ProductDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderDetailsScreen(
	order: Order,
	productViewModel: ProductViewModel,
	orderViewModel: OrderViewModel,
	customerViewModel: CustomerViewModel,
	navController: NavController
) {
	var showProductsDialog by remember { mutableStateOf(false) }
	var showShippingDialog by remember { mutableStateOf(false) }
	
	var currentOrder by remember { mutableStateOf(order) }
	val context = LocalContext.current
	val currentCustomer by remember { mutableStateOf(customerViewModel.getCustomerById(order.customerId!!)) }
	
	Column {
		TopAppBar(
			title = { Text("Order Details") },
			navigationIcon = {
				IconButton(onClick = { navController.navigate(NAVIGATION_SCREEN_ORDER_LIST) }) {
					Icon(
						Icons.AutoMirrored.Filled.ArrowBack,
						contentDescription = "Back to Order List"
					)
				}
			}, actions = {
				IconButton (onClick = {
					createAndShareOrderPdf(context = context, order = currentOrder, customer = currentCustomer!!)
				}){
					Icon(
						imageVector = Icons.Default.Share,
						contentDescription = "Share Order"
					)
				}
			}
		)
		if (!showProductsDialog) {
			Card(
				modifier = Modifier
					.fillMaxWidth()
					.padding(8.dp)
					.weight(2f),
				elevation = CardDefaults.cardElevation(4.dp)
			) {
				Column(modifier = Modifier.padding(horizontal = 16.dp).verticalScroll(rememberScrollState())) {
					Text(
						text = "Customer :",
						style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
						modifier = Modifier.padding(vertical = 4.dp)
					)
					Text(text = "Name:   ${currentCustomer?.customerName}")
					Text(text = "Address:   ${currentCustomer?.customerAddress}")
					Text(text = "City:   ${currentCustomer?.customerCity} / ${currentCustomer?.customerCountry}")
					Text(text = "Tel:   ${currentCustomer?.customerTel}")
					Text(text = "Email:   ${currentCustomer?.customerEmail}")
				}
				
			}
			Card(
				modifier = Modifier
					.fillMaxWidth()
					.padding(8.dp)
					.weight(4f),
				elevation = CardDefaults.cardElevation(4.dp)
			) {
				Row(
					modifier = Modifier
						.fillMaxWidth()
						.padding(vertical = 8.dp, horizontal = 4.dp),
					horizontalArrangement = Arrangement.SpaceBetween
				) {
					Text(
						text = "Product Name",
						modifier = Modifier
							.weight(2f)
							.padding(vertical = 4.dp),
						style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold)
					)
					Text(
						text = "Quantity",
						modifier = Modifier
							.weight(0.8f)
							.padding(vertical = 4.dp),
						textAlign = TextAlign.Center,
						style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold)
					)
					Text(
						text = "    Price",
						modifier = Modifier
							.weight(1.5f)
							.padding(vertical = 4.dp),
						style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold),
						textAlign = TextAlign.Center
						
					)
				}
				LazyColumn(
					modifier = Modifier
						.fillMaxWidth()
				) {
					items(currentOrder.productList) { product ->
						if (product.productQuantity != 0.0)
							Row(
								modifier = Modifier
									.fillMaxWidth()
									.padding(horizontal = 4.dp),
								horizontalArrangement = Arrangement.SpaceBetween
							) {
								Text(
									text = product.productName.uppercase(),
									modifier = Modifier
										.weight(2f)
										.padding(vertical = 4.dp)
								)
								Text(
									text = product.productQuantity.toString() + " kg",
									modifier = Modifier
										.weight(0.7f)
										.padding(vertical = 4.dp),
									textAlign = TextAlign.Center
								)
								Text(
									text = product.totalPrice().toSekFormat(product.productCurrency.uppercase()),
									modifier = Modifier
										.weight(1.5f)
										.padding(vertical = 4.dp),
									textAlign = TextAlign.End
								)
								
							}
					}
				}
			}
			Row(
				modifier = Modifier
					.fillMaxWidth()
					.wrapContentHeight()
					.padding(vertical = 8.dp, horizontal = 24.dp),
				horizontalArrangement = Arrangement.SpaceBetween
			) {
				Row {
					Text(
						text = "Process : ",
						style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold),
						textAlign = TextAlign.End
					)
					Text(
						text = currentOrder.process.toString().uppercase(),
						style = TextStyle(
							fontSize = 18.sp,
							fontWeight = FontWeight.Bold,
							color = Color.White,
							background = Color(getOrderProcessColor(currentOrder.process!!)),
							letterSpacing = 1.sp
						),
						textAlign = TextAlign.Start,
					)
				}
				Text(
					text = "Shipping: ${currentOrder.shipping.toSekFormat(currentOrder.productList[0].productCurrency.uppercase())}",
					style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold),
					textAlign = TextAlign.Center
				)
			}
			Row(
				modifier = Modifier
					.fillMaxWidth()
					.wrapContentHeight()
					.padding(vertical = 8.dp, horizontal = 24.dp),
				horizontalArrangement = Arrangement.End
			) {
				Text(
					text = "Total Price: ${currentOrder.totalPrice().toSekFormat(currentOrder.productList[0].productCurrency.uppercase())}",
					style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold),
					textAlign = TextAlign.Center
				)
			}
			
			Row(
				modifier = Modifier
					.fillMaxWidth()
					.wrapContentHeight()
					.padding(vertical = 8.dp, horizontal = 4.dp),
				horizontalArrangement = Arrangement.SpaceBetween,
			) {
				Button(onClick = {
					showShippingDialog = true
				}) {
					Text("Update Shipping and Process")
				}
				
				AddButton(text = "Update Order") {
					showProductsDialog = true
				}
			}
		}
		
		if (showShippingDialog) ShippingAndProcessDialog(
			order = currentOrder,
			onDismiss = { showShippingDialog = false },
			onSave = { shippingCost, selectedProcess ->
				val updatedOrder =
					currentOrder.copy(shipping = shippingCost, process = selectedProcess)
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