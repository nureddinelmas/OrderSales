package com.nureddinelmas.onlinesales.widgets.order

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.gson.Gson
import com.nureddinelmas.onlinesales.models.Order
import com.nureddinelmas.onlinesales.viewModel.OrderViewModel
import java.nio.charset.StandardCharsets
import java.util.UUID

@Composable
fun OrderListScreen(
	viewModel: OrderViewModel,
	navController: NavController
) {
	val uiState by viewModel.uiState.collectAsState()
	
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
			LazyColumn(
				modifier = Modifier
					.fillMaxSize()
					.background(color = Color(0xFFF0F0F0))
			) {
				items(
					items = uiState.orders,
					key = { UUID.randomUUID().toString() }) { order ->
					OrderItem(order) {
						val gson = Gson()
						val orderJson = gson.toJson(order)
						val encodedOrderJson = java.net.URLEncoder.encode(
							orderJson,
							StandardCharsets.UTF_8.toString()
						)
						navController.navigate("details/${encodedOrderJson}")
					}
					
				}
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


@Composable
fun OrderItem(order: Order, onClick: () -> Unit) {
	
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
					modifier = Modifier.padding(vertical = 4.dp).weight(1f)
				)
				Text(
					text = "Quantity",
					modifier = Modifier.padding(vertical = 4.dp).weight(1f),
					textAlign = TextAlign.Center
				)
				Text(
					text = "Price",
					modifier = Modifier.padding(vertical = 4.dp).weight(1f)
				)
			}
			Row(
				modifier = Modifier
					.fillMaxWidth()
					.padding(vertical = 8.dp),
				horizontalArrangement = Arrangement.SpaceBetween
			) {
				Text(
					text = "${order.customer.customerName}",
					modifier = Modifier.padding(vertical = 4.dp).weight(1f)
				)
				
				Text(
					text = "${order.totalQuantity()}",
					modifier = Modifier.padding(vertical = 4.dp).weight(1f),
					textAlign = TextAlign.Center
				)
				
				Text(
					text = "${order.totalPrice()}  ${order.productList[0].productCurrency.uppercase()}",
					modifier = Modifier.padding(vertical = 4.dp).weight(1f)
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
