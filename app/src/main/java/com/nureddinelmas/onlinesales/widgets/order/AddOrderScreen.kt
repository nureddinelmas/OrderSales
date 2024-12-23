package com.nureddinelmas.onlinesales.widgets.order

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.nureddinelmas.onlinesales.models.Order
import com.nureddinelmas.onlinesales.models.Product
import com.nureddinelmas.onlinesales.viewModel.OrderViewModel
import com.nureddinelmas.onlinesales.viewModel.ProductViewModel


@Composable
fun AddOrderScreen(
    productViewModel: ProductViewModel,
    navController: NavController,
    orderViewModel: OrderViewModel
) {
    var showDialog by remember { mutableStateOf(false) }
    val selectedProducts = remember { mutableStateListOf<Product>() }
    Scaffold { padding ->
        Column(modifier = Modifier.padding(padding)) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Product Name", modifier = Modifier.weight(1f))
                        Text(text = "Quantity", modifier = Modifier.weight(1f))
                    }
                    LazyColumn {
                        items(selectedProducts) { product ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = product.productName,
                                    modifier = Modifier.weight(1f)
                                )
                                Text(
                                    text = product.productQuantity.toString(),
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                    Button(onClick = {
                        showDialog = true
                    }) {
                        Text("Add Product")
                    }
                }
            }

            if (showDialog) {
                ProductDialog(
                    viewModel = productViewModel,
                    onDismiss = { showDialog = false },
                    selectedProducts = selectedProducts
                )
            }

            Button(
                onClick = {
                    val order = Order(customer = null, productList = selectedProducts)
                    orderViewModel.addOrder(order)
                },
                modifier = Modifier.padding(5.dp)
            ) { Text("Save") }
        }
        // ProductSearchScreen(modifier = Modifier.padding(padding))
        // Add your code here
    }
}


@Composable
fun ProductDialog(
    viewModel: ProductViewModel,
    onDismiss: () -> Unit,
    selectedProducts: MutableList<Product>
) {
    val products by viewModel.uiState.collectAsState()

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
        ) {
            Box {
                LazyColumn {
                    items(products.products) { product ->
                        ProductSearchItem(
                            product = product,
                            viewModel = viewModel,
                            selectedProducts
                        )
                    }
                }
                Button(
                    onClick = {
                        onDismiss()
                    },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(5.dp)
                ) { Text("Back") }

            }
        }
    }
}


@Composable
fun ProductSearchItem(
    product: Product,
    viewModel: ProductViewModel,
    selectedProducts: MutableList<Product>
) {
    var quantity by remember { mutableIntStateOf(0) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = product.productName)

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { if (quantity > 0) quantity-- }) {
                Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "Decrease Quantity")
            }

            Text(text = quantity.toString(), modifier = Modifier.padding(horizontal = 16.dp))

            IconButton(onClick = { quantity++ }) {
                Icon(Icons.Default.KeyboardArrowRight, contentDescription = "Increase Quantity")
            }

            Button(
                onClick = {
                    if (quantity > 0) {
                        val productList = Product(
                            productName = product.productName,
                            productQuantity = quantity,
                            productPrice = product.productPrice,
                            productCurrency = product.productCurrency,
                            productComment = product.productComment
                        )
                        selectedProducts.add(productList)
                    }

                },
                modifier = Modifier
                    .padding(5.dp)
            ) { Text("Add") }
        }
    }
}
