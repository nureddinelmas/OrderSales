package com.nureddinelmas.onlinesales.widgets.customer

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.google.firebase.firestore.FirebaseFirestore
import com.nureddinelmas.onlinesales.CONSTANTS_FIREBASE_COLLECTION_PRODUCTS
import com.nureddinelmas.onlinesales.models.Product
import java.util.UUID

@Composable
fun ProductSearchScreen(modifier: Modifier) {
    var searchTerm by remember { mutableStateOf("") }
    var productList by remember { mutableStateOf(listOf<Product>()) }
    val selectedProducts = remember { mutableStateListOf<Product>() }

    Column {
        TextField(
            value = searchTerm,
            onValueChange = {
                searchTerm = it
                fetchProducts(it) { products ->
                    productList = products
                }
            },
            label = { Text("Search Product") }
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(0xFFF0F0F0))
        ) {
            items(
                items = productList,
                key = { UUID.randomUUID().toString() }) { product ->
                Text(text = product.productName, modifier = Modifier.clickable {
                    selectedProducts.add(product)
                    searchTerm = ""
                })
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(0xFFF0F0F0))
        ) {
            items(
                items = selectedProducts,
                key = { UUID.randomUUID().toString() }) { product ->
                Text(text = product.productName)
            }
        }

        Button(onClick = { saveSelectedProducts(selectedProducts) }) {
            Text("Save Products")
        }
    }

}

fun fetchProducts(searchTerm: String, onResult: (List<Product>) -> Unit) {
    val db = FirebaseFirestore.getInstance()
    db.collection(CONSTANTS_FIREBASE_COLLECTION_PRODUCTS)
        .whereEqualTo("productName", searchTerm)
        .get()
        .addOnSuccessListener { result ->
            val products = result.map { doc ->
                Product(productId = doc.id, productName = doc.getString("productName") ?: "")
            }
            onResult(products)
        }
        .addOnFailureListener { exception ->
            Log.d("!!!", "Error getting documents: ", exception)
            onResult(emptyList())
        }
}


fun saveSelectedProducts(selectedProducts: List<Product>) {
    val db = FirebaseFirestore.getInstance()
    selectedProducts.forEach { product ->
        db.collection("selectedProducts")
            .add(product)
            .addOnSuccessListener { documentReference ->
                Log.d("SaveProduct", "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w("SaveProduct", "Error adding document", e)
            }
    }
}
