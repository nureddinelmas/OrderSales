package com.nureddinelmas.onlinesales.widgets.customer


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.nureddinelmas.onlinesales.models.Customer
import com.nureddinelmas.onlinesales.viewModel.CustomerViewModel
import com.nureddinelmas.onlinesales.viewModel.OrderViewModel
import java.util.UUID

@Composable
fun CustomerListScreen(
	customerViewModel: CustomerViewModel,
	navController: NavController,
	orderViewModel: OrderViewModel
) {
	val uiState by customerViewModel.uiState.collectAsState()
	var showCustomerDialog by remember { mutableStateOf(false) }
	var selectedCustomer by remember { mutableStateOf(Customer()) }
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
		
		uiState.customer.isNotEmpty() -> {
			if (!showCustomerDialog) {
				Card(
					modifier = Modifier
						.fillMaxSize()
						.padding(vertical = 8.dp, horizontal = 8.dp)
				) {
					Column(
						modifier = Modifier.padding(vertical = 8.dp, horizontal = 8.dp)
					) {
						Row(
							verticalAlignment = Alignment.CenterVertically,
							horizontalArrangement = Arrangement.SpaceBetween
						) {
							Text(
								text = "Customer Name",
								modifier = Modifier
									.padding(vertical = 4.dp, horizontal = 8.dp)
									.weight(2f)
								, textAlign = TextAlign.Start,
								style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold)
							)
							Text(
								text = "City",
								modifier = Modifier
									.padding(vertical = 4.dp)
									.weight(1f),
								style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold)
							)
						}
						LazyColumn(
							modifier = Modifier
								.fillMaxSize()
								.background(color = Color(0xFFF0F0F0))
						) {
							items(
								items = uiState.customer,
								key = { UUID.randomUUID().toString() }) { customer ->
								
								CustomerItem(customer) {
									showCustomerDialog = true
									selectedCustomer = customer
								}
							}
							
						}
					}
				}
			}
		}
		
		uiState.customer.isEmpty() -> {
			Box(
				modifier = Modifier.fillMaxSize(),
				contentAlignment = Alignment.Center
			) {
				Text(text = "No Customers Found", style = TextStyle(fontSize = 20.sp))
			}
		}
	}
	
	
	if (showCustomerDialog) AddNewCustomerScreen(
		navController,
		customer = selectedCustomer,
		customerViewModel,
		true,
		orderViewModel = orderViewModel
	) {
		showCustomerDialog = false
	}
}

@Composable
fun CustomerItem(customer: Customer, onClick: () -> Unit) {
	Column(modifier = Modifier
		.padding(vertical = 8.dp, horizontal = 8.dp)
		.clickable {
			onClick()
		}) {
		Row {
			Text(
				text = "${customer.customerName}",
				modifier = Modifier
					.padding(vertical = 4.dp)
					.weight(2f)
			)
			
			Text(
				text = "${customer.customerCity}",
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