package com.nureddinelmas.onlinesales.widgets.product

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
import com.nureddinelmas.onlinesales.models.Product

@Composable
fun AddNewProductScreen(navController: NavController, onSaveProduct: (Product) -> Unit) {
    val productId = remember { mutableStateOf("") }
    val productName = remember { mutableStateOf("") }
    val productPrice = remember { mutableStateOf("") }
    val productCurrency = remember { mutableStateOf("") }
    val productComment = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = productName.value,
            onValueChange = { productName.value = it },
            label = { Text("Product Name") },
            modifier = Modifier.padding(vertical = 4.dp)
        )
        OutlinedTextField(
            value = productPrice.value,
            onValueChange = { productPrice.value = it },
            label = { Text("Product Price") },
            modifier = Modifier.padding(vertical = 4.dp)
        )
        OutlinedTextField(
            value = productCurrency.value,
            onValueChange = { productCurrency.value = it },
            label = { Text("Product Currency") },
            modifier = Modifier.padding(vertical = 4.dp)
        )
        OutlinedTextField(
            value = productComment.value,
            onValueChange = { productComment.value = it },
            label = { Text("Product Comment") },
            modifier = Modifier.padding(vertical = 4.dp)
        )
        Button(
            onClick = {
                val newProduct = Product(
                    productName = productName.value,
                    productPrice = productPrice.value,
                    productCurrency = productCurrency.value,
                    productComment = productComment.value
                )
                onSaveProduct(newProduct)
                navController.navigate("productList")
            },
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Text("Save Product")
        }
    }
}