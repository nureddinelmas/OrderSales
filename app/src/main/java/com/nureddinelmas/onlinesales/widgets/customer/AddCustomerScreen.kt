package com.nureddinelmas.onlinesales.widgets.customer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nureddinelmas.onlinesales.models.Customer
import com.nureddinelmas.onlinesales.models.Product

@Composable
fun AddNewCustomerScreen(navController: NavController, onSaveProduct: (Customer) -> Unit) {
    val customerName = remember { mutableStateOf("") }
    val customerMobilePhone = remember { mutableStateOf("") }
    val customerAddress = remember { mutableStateOf("") }
    val customerCity = remember { mutableStateOf("") }
    val customerCountry = remember { mutableStateOf("") }
    val customerEmail = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = customerName.value,
            onValueChange = { customerName.value = it },
            label = { Text("Name") },
            modifier = Modifier.padding(vertical = 4.dp)
        )
        OutlinedTextField(
            value = customerMobilePhone.value,
            onValueChange = { customerMobilePhone.value = it },
            label = { Text("Mobile Phone") },
            modifier = Modifier.padding(vertical = 4.dp)
        )
        OutlinedTextField(
            value = customerAddress.value,
            onValueChange = { customerAddress.value = it },
            label = { Text("Address") },
            modifier = Modifier.padding(vertical = 4.dp)
        )
        OutlinedTextField(
            value = customerCity.value,
            onValueChange = { customerCity.value = it },
            label = { Text("City") },
            modifier = Modifier.padding(vertical = 4.dp)
        )
        OutlinedTextField(
            value = customerCountry.value,
            onValueChange = { customerCountry.value = it },
            label = { Text("Country") },
            modifier = Modifier.padding(vertical = 4.dp)
        )
        OutlinedTextField(
            value = customerEmail.value,
            onValueChange = { customerEmail.value = it },
            label = { Text("Email") },
            modifier = Modifier.padding(vertical = 4.dp)
        )
        Button(
            onClick = {
               val newCustomer = Customer(
                   customerName = customerName.value,
                    customerCountry = customerCountry.value,
                    customerEmail = customerEmail.value,
                    customerCity = customerCity.value,
                    customerAddress = customerAddress.value,
                    customerTel = customerMobilePhone.value,
                )
                onSaveProduct(newCustomer)
                navController.popBackStack()
            },
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Text("Save Customer")
        }
    }
}