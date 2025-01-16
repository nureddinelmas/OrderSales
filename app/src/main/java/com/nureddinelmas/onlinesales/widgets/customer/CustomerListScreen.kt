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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.nureddinelmas.onlinesales.models.Customer
import com.nureddinelmas.onlinesales.viewModel.CustomerViewModel
import com.nureddinelmas.onlinesales.viewModel.OrderViewModel
import java.util.UUID

@Composable
fun CustomerListScreen(customerViewModel: CustomerViewModel, navController: NavController, orderViewModel: OrderViewModel) {
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
            Column {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = Color(0xFFF0F0F0))
                ) {
                    items(
                        items = uiState.customer,
                        key = { UUID.randomUUID().toString() }) { customer ->
                        if (!showCustomerDialog) {
                            CustomerItem(customer) {
                                showCustomerDialog = true
                                selectedCustomer = customer
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
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .clickable {
                onClick()
            }
    ) {

        Column(modifier = Modifier.padding(vertical = 8.dp, horizontal = 8.dp)) {
            Text(
                text = "Name: ${customer.customerName}",
                modifier = Modifier.padding(vertical = 4.dp)
            )
            Text(
                text = "Telefon Number: ${customer.customerTel}",
                modifier = Modifier.padding(vertical = 4.dp)
            )
            Text(
                text = "Address: ${customer.customerAddress}",
                modifier = Modifier.padding(vertical = 4.dp)
            )
            Text(
                text = "City: ${customer.customerCity}",
                modifier = Modifier.padding(vertical = 4.dp)
            )
            Text(
                text = "Country: ${customer.customerCountry}",
                modifier = Modifier.padding(vertical = 4.dp)
            )

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