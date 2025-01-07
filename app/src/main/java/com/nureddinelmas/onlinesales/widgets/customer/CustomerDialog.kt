package com.nureddinelmas.onlinesales.widgets.customer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.nureddinelmas.onlinesales.models.Customer
import com.nureddinelmas.onlinesales.viewModel.CustomerViewModel

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