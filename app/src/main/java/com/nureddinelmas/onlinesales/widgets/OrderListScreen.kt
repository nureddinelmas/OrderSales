package com.nureddinelmas.onlinesales.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.nureddinelmas.onlinesales.viewModel.OrderViewModel
import java.util.UUID

@Composable
fun OrderListScreen(viewModel: OrderViewModel, paddingValues: PaddingValues){
	val uiState by viewModel.uiState.collectAsState()
	when {
		uiState.isLoading -> CircularProgressIndicator()
		(uiState.error != null) -> {
			Text(text = "Error: ${uiState.error}")
		}
		
		uiState.orders.isNotEmpty() -> {
			LazyColumn(
				contentPadding = paddingValues,
				modifier = Modifier
					.fillMaxSize()
					.background(color = Color(0xFFF0F0F0))
			) {
				items(
					items = uiState.orders,
					key = { UUID.randomUUID().toString() }) { order ->
					Row(
						modifier = Modifier
							.padding(vertical = 5.dp, horizontal = 2.dp)
							.fillMaxSize()
							.border(width = 1.dp, color = Color.Blue)
					
					) {
						Icon(
							Icons.Default.AccountBox,
							contentDescription = "Add Order",
							modifier = Modifier.size(45.dp),
						)
						Button(
							onClick = {
								viewModel.deleteOrder(order.orderId.toString())
							}
						) { Text(order.orderId.toString()) }
					}
				}
			}
		}
	}
}
