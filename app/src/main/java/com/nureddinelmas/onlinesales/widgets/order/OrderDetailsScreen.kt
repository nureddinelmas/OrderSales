package com.nureddinelmas.onlinesales.widgets.order

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nureddinelmas.onlinesales.models.Order
import com.nureddinelmas.onlinesales.models.Product
import com.nureddinelmas.onlinesales.viewModel.OrderViewModel
import com.nureddinelmas.onlinesales.viewModel.ProductViewModel
import com.nureddinelmas.onlinesales.widgets.product.ProductDialog
import com.nureddinelmas.onlinesales.widgets.product.ProductItems

@Composable
fun OrderDetailsScreen(
	order: Order,
	productViewModel: ProductViewModel,
	orderViewModel: OrderViewModel
) {
	var showProductsDialog by remember { mutableStateOf(false) }
	Card(
		modifier = Modifier
			.fillMaxWidth()
			.padding(8.dp),
		elevation = CardDefaults.cardElevation(4.dp)
	) {
		Column(modifier = Modifier.padding(16.dp)) {
			Text(
				text = "Customer :",
				style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
				modifier = Modifier.padding(vertical = 8.dp)
			)
			Text(text = "Name:   ${order.customer.customerName}")
			Text(text = "Address:   ${order.customer.customerAddress}")
			Text(text = "City:   ${order.customer.customerCity}")
			Text(text = "Country:   ${order.customer.customerCountry}")
			Text(text = "Tel:   ${order.customer.customerTel}")
			Text(text = "Email:   ${order.customer.customerEmail}")
			
			
			HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), thickness = 2.dp)
			Row(
				modifier = Modifier
					.fillMaxWidth()
					.padding(vertical = 8.dp, horizontal = 4.dp),
				horizontalArrangement = Arrangement.SpaceBetween
			) {
				Text(
					text = "Product Name",
					modifier = Modifier
						.weight(1f)
						.padding(vertical = 4.dp),
					style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Bold)
				)
				Text(
					text = "Quantity",
					modifier = Modifier
						.weight(1f)
						.padding(vertical = 4.dp),
					textAlign = TextAlign.Center,
					style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Bold)
				)
				Text(
					text = "Price",
					modifier = Modifier
						.weight(1f)
						.padding(vertical = 4.dp),
					style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Bold)
				)
			}
			order.productList.forEach {
				Row(
					modifier = Modifier.fillMaxWidth(),
					horizontalArrangement = Arrangement.SpaceBetween
				) {
					Text(
						text = it.productName.uppercase(),
						modifier = Modifier
							.weight(1f)
							.padding(vertical = 4.dp)
					)
					Text(
						text = it.productQuantity.toString() + " kg",
						modifier = Modifier
							.weight(1f)
							.padding(vertical = 4.dp),
						textAlign = TextAlign.Center
					)
					Text(
						text = it.totalPrice().toString() + " ${it.productCurrency.uppercase()}",
						modifier = Modifier
							.weight(1f)
							.padding(vertical = 4.dp)
					)
					
				}
			}
			
			Row(
				modifier = Modifier
					.fillMaxWidth()
					.padding(vertical = 8.dp, horizontal = 4.dp),
				horizontalArrangement = Arrangement.End
			) {
				Text(
					text = "Total Price: ${order.totalPrice()} ${order.productList[0].productCurrency.uppercase()}",
					style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold),
					textAlign = TextAlign.Center
				)
			}
			Row(
				horizontalArrangement = Arrangement.Start,
			) {
				AddButton(text = "Update Order") {
					showProductsDialog = true
				}
			}
			if (showProductsDialog) ProductDialog(
				viewModel = productViewModel,
				onDismiss = { showProductsDialog = false },
				selectedProducts = order.productList,
				onSave = { list ->
					order.productList = list as SnapshotStateList<Product>
					orderViewModel.updateOrder(order)
					Log.d("!!!", "OrderDetailsScreen: ${order.productList}")
				}
			)
		}
	}
}