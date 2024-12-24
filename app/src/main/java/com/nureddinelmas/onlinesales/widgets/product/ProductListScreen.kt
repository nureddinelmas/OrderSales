package com.nureddinelmas.onlinesales.widgets.product


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nureddinelmas.onlinesales.models.Product
import com.nureddinelmas.onlinesales.viewModel.ProductViewModel
import java.util.UUID

@Composable
fun ProductListScreen(productViewModel: ProductViewModel) {
    val uiState by productViewModel.uiState.collectAsState()
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

        uiState.products.isNotEmpty() -> {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color(0xFFF0F0F0))
            ) {
                items(
                    items = uiState.products,
                    key = { UUID.randomUUID().toString() }) { product ->
                    ProductItem(product)
                }
            }
        }
        
        uiState.products.isEmpty() -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "No Products Found", style = TextStyle(fontSize = 20.sp))
            }
        }
    }


}

@Composable
fun ProductItem(product: Product) {
    Column(modifier = Modifier.padding(vertical = 8.dp, horizontal = 8.dp)) {
        Text(text = "Product Name: ${product.productName}", modifier = Modifier.padding(vertical = 4.dp))
        Text(text = "Product Price: ${product.productPrice}", modifier = Modifier.padding(vertical = 4.dp))
        Text(text = "Product Currency: ${product.productCurrency}", modifier = Modifier.padding(vertical = 4.dp))
        Text(text = "Product Comment: ${product.productComment}", modifier = Modifier.padding(vertical = 4.dp))
        HorizontalDivider(thickness = 1.dp, color = Color.Gray, modifier = Modifier.padding(vertical = 8.dp).weight(2f))
    }
}