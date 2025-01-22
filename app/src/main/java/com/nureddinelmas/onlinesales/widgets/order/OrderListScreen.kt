package com.nureddinelmas.onlinesales.widgets.order

import android.annotation.SuppressLint
import android.content.Context
import android.print.PrintManager
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.gson.Gson
import com.nureddinelmas.onlinesales.ViewPrintAdapter
import com.nureddinelmas.onlinesales.models.Order
import com.nureddinelmas.onlinesales.ui.theme.OnlineSalesTheme
import com.nureddinelmas.onlinesales.viewModel.CustomerViewModel
import com.nureddinelmas.onlinesales.viewModel.OrderViewModel
import java.nio.charset.StandardCharsets
import java.util.UUID

@SuppressLint("DefaultLocale")
@Composable
fun OrderListScreen(
	orderViewModel: OrderViewModel,
	customerViewModel: CustomerViewModel,
	navController: NavController
) {
	val uiState by orderViewModel.uiState.collectAsState()
	
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
		
		
		uiState.orders.isNotEmpty() -> {
			Column {
//				val context = navController.context
//				 val orderListView = ComposeView(context).apply {
//					 setContent {
				
				LazyColumn(
					modifier = Modifier
						.fillMaxSize()
						.background(color = Color(0xFFF0F0F0))
						.weight(10f)
				) {
					items(
						items = uiState.orders,
						key = { UUID.randomUUID().toString() }) { order ->
						if (order.totalQuantity() != 0.0) OrderItem(
							order,
							onUpdateClick = {
								val gson = Gson()
								val orderJson = gson.toJson(order)
								val encodedOrderJson = java.net.URLEncoder.encode(
									orderJson,
									StandardCharsets.UTF_8.toString()
								)
								navController.navigate("update/${encodedOrderJson}")
							},
							onClick = {
								val gson = Gson()
								val orderJson = gson.toJson(order)
								val encodedOrderJson = java.net.URLEncoder.encode(
									orderJson,
									StandardCharsets.UTF_8.toString()
								)
								navController.navigate("details/${encodedOrderJson}")
								
							},
							customerName = customerViewModel.getCustomerById(order.customerId!!)?.customerName ?: "")
					}
				}
//					 }
//				}

//				Button(
//					modifier = Modifier.fillMaxWidth(),
//					onClick = {
//					val printManager =
//						navController.context.getSystemService(Context.PRINT_SERVICE) as PrintManager
//					val printAdapter = ViewPrintAdapter(navController.context, orderListView)
//					printManager.print("Order List", printAdapter, null)
//				}) {
//					Text("Print Orders")
//				}
				Text(
					text = "Total Orders: ${uiState.orders.size} / Total Price: ${String.format("%.2f", uiState.orders.sumOf {
						if (it.productList.isEmpty()) 0.0 else it.totalPrice() })} ${uiState.orders[0].productList[0].productCurrency.uppercase()}",
					modifier = Modifier
						.fillMaxWidth()
						.padding(8.dp),
					textAlign = TextAlign.Center,
					style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold)
				)
			}
		}
		
		uiState.orders.isEmpty() -> {
			Box(
				modifier = Modifier.fillMaxSize(),
				contentAlignment = Alignment.Center
			) {
				Text(text = "No Orders Found", style = TextStyle(fontSize = 20.sp))
			}
		}
	}
}


@SuppressLint("DefaultLocale")
@Composable
fun OrderItem(order: Order, onClick: () -> Unit, onUpdateClick: () -> Unit, customerName: String) {
	
	Card(
		modifier = Modifier
			.fillMaxWidth()
			.padding(8.dp),
		elevation = CardDefaults.cardElevation(4.dp)
	) {
		Column(modifier = Modifier
			.clickable { onClick() }
			.padding(8.dp)) {
			Row(
				modifier = Modifier
					.fillMaxWidth()
					.padding(vertical = 8.dp),
				horizontalArrangement = Arrangement.SpaceBetween
			) {
				Text(
					text = "Customer Name",
					modifier = Modifier
						.padding(vertical = 4.dp)
						.weight(1.5f),
					textAlign = TextAlign.Start
				)
				Text(
					text = "Quantity",
					modifier = Modifier
						.padding(vertical = 4.dp)
						.weight(1f),
					textAlign = TextAlign.Center
				)
				Text(
					text = "Price",
					modifier = Modifier
						.padding(vertical = 4.dp)
						.weight(1f),
					textAlign = TextAlign.Center
				)
			}
			Row(
				modifier = Modifier
					.fillMaxWidth()
					.padding(vertical = 8.dp),
				horizontalArrangement = Arrangement.SpaceBetween
			) {
				Text(
					text = customerName.uppercase(),
					modifier = Modifier
						.padding(vertical = 4.dp)
						.weight(1.5f),
					textAlign = TextAlign.Start
				)
				
				Text(
					text = "${order.totalQuantity()}",
					modifier = Modifier
						.padding(vertical = 4.dp)
						.weight(1f),
					textAlign = TextAlign.Center
				)
				
				Text(
					text = String.format("%.2f  %s", order.totalPrice(), order.productList[0].productCurrency.uppercase()),
					modifier = Modifier
						.padding(vertical = 4.dp)
						.weight(1f),
					textAlign = TextAlign.Center
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
}
