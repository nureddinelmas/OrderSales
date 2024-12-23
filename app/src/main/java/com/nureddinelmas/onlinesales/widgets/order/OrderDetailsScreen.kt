package com.nureddinelmas.onlinesales.widgets.order

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.nureddinelmas.onlinesales.models.Order

@Composable
fun OrderDetailsScreen(order: Order) {
    Column {
        Text(text = "Order Id : ${order.orderId}")
        Text(text = "Customer Name : ${order.customer}")
    }
}