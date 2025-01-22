package com.nureddinelmas.onlinesales.widgets.customer

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nureddinelmas.onlinesales.NAVIGATION_SCREEN_CUSTOMER_LIST
import com.nureddinelmas.onlinesales.models.Customer
import com.nureddinelmas.onlinesales.viewModel.CustomerViewModel
import com.nureddinelmas.onlinesales.viewModel.OrderViewModel

@Composable
fun AddNewCustomerScreen(
	navController: NavController,
	customer: Customer,
	viewModel: CustomerViewModel,
	isUpdateCustomer: Boolean,
	orderViewModel: OrderViewModel,
	onButtonClick: () -> Unit,
) {
	val customerName = remember { mutableStateOf(customer.customerName) }
	val customerMobilePhone = remember { mutableStateOf(customer.customerTel) }
	val customerAddress = remember { mutableStateOf(customer.customerAddress) }
	val customerCity = remember { mutableStateOf(customer.customerCity) }
	val customerCountry = remember { mutableStateOf(customer.customerCountry) }
	val customerEmail = remember { mutableStateOf(customer.customerEmail) }
	
	var isReadable by remember { mutableStateOf(isUpdateCustomer) }
	var buttonTextName by remember { mutableStateOf(if (isUpdateCustomer) "Change" else "Save") }
	Column(
		modifier = Modifier
			.fillMaxSize()
			.padding(16.dp),
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		OutlinedTextField(
			readOnly = isReadable,
			value = customerName.value ?: "",
			onValueChange = { customerName.value = it },
			label = { Text("Name") },
			modifier = Modifier
				.padding(vertical = 4.dp)
				.fillMaxWidth()
		)
		OutlinedTextField(
			readOnly = isReadable,
			value = customerMobilePhone.value ?: "",
			onValueChange = { customerMobilePhone.value = it },
			label = { Text("Mobile Phone") },
			modifier = Modifier
				.padding(vertical = 4.dp)
				.fillMaxWidth()
		)
		OutlinedTextField(
			readOnly = isReadable,
			value = customerAddress.value ?: "",
			onValueChange = { customerAddress.value = it },
			label = { Text("Address") },
			modifier = Modifier
				.padding(vertical = 4.dp)
				.fillMaxWidth(),
			maxLines = 10,
			minLines = 3
		)
		OutlinedTextField(
			readOnly = isReadable,
			value = customerCity.value ?: "",
			onValueChange = { customerCity.value = it },
			label = { Text("City") },
			modifier = Modifier
				.padding(vertical = 4.dp)
				.fillMaxWidth()
		)
		OutlinedTextField(
			readOnly = isReadable,
			value = customerCountry.value ?: "",
			onValueChange = { customerCountry.value = it },
			label = { Text("Country") },
			modifier = Modifier
				.padding(vertical = 4.dp)
				.fillMaxWidth()
		)
		OutlinedTextField(
			readOnly = isReadable,
			value = customerEmail.value ?: "",
			onValueChange = { customerEmail.value = it },
			label = { Text("Email") },
			modifier = Modifier
				.padding(vertical = 4.dp)
				.fillMaxWidth()
		)
		
		Row(
			modifier = Modifier
				.padding(vertical = 8.dp)
				.fillMaxWidth(),
			horizontalArrangement = Arrangement.SpaceBetween
		) {
			Button(
				onClick = {
					navController.navigate(NAVIGATION_SCREEN_CUSTOMER_LIST)
				},
				modifier = Modifier
					.padding(8.dp)
					.weight(1f)
			) {
				Text("Cancel")
			}
			Button(
				onClick = {
					val newCustomer = Customer(
						customerId = if (isUpdateCustomer) customer.customerId else null,
						customerName = customerName.value,
						customerCountry = customerCountry.value,
						customerEmail = customerEmail.value,
						customerCity = customerCity.value,
						customerAddress = customerAddress.value,
						customerTel = customerMobilePhone.value,
					)
					
					when (buttonTextName) {
						"Update" -> {
							viewModel.updateCustomer(newCustomer)
							onButtonClick()
						}
						
						"Save" -> {
							viewModel.addCustomer(newCustomer)
							navController.popBackStack()
						}
						
						"Change" -> {
							isReadable = !isReadable
							buttonTextName = "Update"
						}
					}
				},
				modifier = Modifier
					.padding(vertical = 8.dp)
					.weight(1f)
			) {
				Text(buttonTextName)
			}
		}
		
		if (isUpdateCustomer && !orderViewModel.checkCustomerExistInOrders(customer.customerId!!)) {
			
			var showDialog by remember { mutableStateOf(false) }
			
			if (showDialog) {
				AlertDialog(
					onDismissRequest = { showDialog = false },
					title = { Text("Delete Customer") },
					text = { Text("Are you sure you want to delete this customer?") },
					confirmButton = {
						TextButton(
							onClick = {
								viewModel.deleteCustomer(customer.customerId)
								onButtonClick()
								showDialog = false
							}
						) {
							Text("Yes")
						}
					},
					dismissButton = {
						TextButton(
							onClick = { showDialog = false }
						) {
							Text("No")
						}
					}
				)
			}
			Button(
				onClick = {
					showDialog = true
				},
				modifier = Modifier
					.padding(2.dp)
					.fillMaxWidth()
			) {
				Text("Delete Customer")
			}
		}
		
		
	}
}